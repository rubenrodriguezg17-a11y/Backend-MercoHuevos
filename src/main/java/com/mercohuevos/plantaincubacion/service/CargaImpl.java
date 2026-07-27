package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.AsignacionMaquinaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.AsignacionMaquinaResponseDTO;
import com.mercohuevos.plantaincubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.CargaResponseDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.mapper.IAsignacionCargaMaquinaMapper;
import com.mercohuevos.plantaincubacion.model.AsignacionCargaMaquina;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.Maquina;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.IAsignacionCargaMaquinaRepository;
import com.mercohuevos.plantaincubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.IMaquinaRepository;
import com.mercohuevos.plantaincubacion.repository.IStockIncubableRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargaImpl implements ICargaService {

    private static final int CAPACIDAD_BANDEJA = 96;

    private final ICargaRepository cargaRepo;
    private final IAsignacionCargaMaquinaRepository asignacionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final IMaquinaRepository maquinaRepo;
    private final IStockIncubableRepository stockRepo;
    private final IAsignacionCargaMaquinaMapper asignacionMapper;

    @Override
    public CargaResponseDTO crear(CargaRequestDTO request) {

        FusionLote fusionLote = fusionLoteRepo.findById(request.idFusionLote())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fusion de lote no encontrada: " + request.idFusionLote()));

        CategoriaEmbandejado categoria = categoriaRepo.findById(request.idCategoriaEmbandejado())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de embandejado no encontrada: " + request.idCategoriaEmbandejado()));

        validarSumaAsignaciones(request);
        StockIncubable stock = validarYObtenerStockDisponible(fusionLote, categoria, request.fechaCarga(), request.cantidadInicial());

        Carga carga = new Carga();
        carga.setFusionLote(fusionLote);
        carga.setCategoriaEmbandejado(categoria);
        carga.setCantidadInicial(request.cantidadInicial());
        carga.setFechaCarga(request.fechaCarga());
        carga.setFechaTransferenciaNacedora(request.fechaCarga().plusDays(18));
        carga.setFechaNacimiento(request.fechaCarga().plusDays(21));
        carga.setEstado(EstadoCarga.EN_INCUBACION);

        Carga guardada = cargaRepo.save(carga);

        List<AsignacionCargaMaquina> asignaciones = request.maquinas().stream()
                .map(m -> construirAsignacion(m, guardada))
                .toList();
        asignacionRepo.saveAll(asignaciones);

        descontarStock(stock, request.cantidadInicial());

        return construirResponseCompleto(guardada, asignaciones);
    }

    @Override
    @Transactional
    public CargaResponseDTO obtenerPorId(Long id) {
        Carga carga = buscarCarga(id);
        List<AsignacionCargaMaquina> asignaciones = asignacionRepo.findByCarga(carga);
        return construirResponseCompleto(carga, asignaciones);
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private void validarSumaAsignaciones(CargaRequestDTO request) {
        int sumaAsignada = request.maquinas().stream()
                .mapToInt(AsignacionMaquinaRequestDTO::cantidad)
                .sum();

        if (sumaAsignada != request.cantidadInicial()) {
            throw new IllegalArgumentException(
                    "La suma de las cantidades asignadas a maquinas (" + sumaAsignada +
                    ") debe ser igual a la cantidad inicial de la carga (" + request.cantidadInicial() + ")");
        }
    }

    private StockIncubable validarYObtenerStockDisponible(
            FusionLote fusionLote, CategoriaEmbandejado categoria, LocalDate fecha, Integer cantidadSolicitada) {

        StockIncubable stock = stockRepo
                .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(
                        fusionLote, categoria, fecha.plusDays(1))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay stock incubable registrado para este lote y categoria"));

        if (stock.getStockActual() < cantidadSolicitada) {
            throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + stock.getStockActual() +
                    ", solicitado: " + cantidadSolicitada);
        }

        return stock;
    }

    private void descontarStock(StockIncubable stock, Integer cantidad) {
        stock.setCargaIncubadora(stock.getCargaIncubadora() + cantidad);
        stock.setStockActual(stock.getStockActual() - cantidad);
        stockRepo.save(stock);
    }

    private AsignacionCargaMaquina construirAsignacion(AsignacionMaquinaRequestDTO asignacionReq, Carga carga) {

        Maquina maquina = maquinaRepo.findById(asignacionReq.idMaquina())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Maquina no encontrada: " + asignacionReq.idMaquina()));

        if (maquina.getTipo() != TipoMaquina.INCUBADORA) {
            throw new IllegalArgumentException(
                    "La maquina " + maquina.getNumero() + " no es una incubadora");
        }

        int yaAsignado = asignacionRepo.sumAsignadoActivoPorMaquina(
                maquina, FaseAsignacion.INCUBACION, EstadoCarga.FINALIZADA);        int disponible = maquina.getCapacidadMaxima() - yaAsignado;

        if (asignacionReq.cantidad() > disponible) {
            throw new IllegalArgumentException(
                    "Capacidad insuficiente en incubadora " + maquina.getNumero() +
                    ". Disponible: " + disponible + ", solicitado: " + asignacionReq.cantidad());
        }

        AsignacionCargaMaquina asignacion = new AsignacionCargaMaquina();
        asignacion.setCarga(carga);
        asignacion.setMaquina(maquina);
        asignacion.setFase(FaseAsignacion.INCUBACION);
        asignacion.setCantidadAsignada(asignacionReq.cantidad());

        return asignacion;
    }

    private CargaResponseDTO construirResponseCompleto(Carga carga, List<AsignacionCargaMaquina> asignaciones) {

        List<AsignacionMaquinaResponseDTO> asignacionesDTO = asignaciones.stream()
                .map(asignacionMapper::toResponseDTO)
                .toList();

        int bandejasCompletas = carga.getCantidadInicial() / CAPACIDAD_BANDEJA;
        int residuo = carga.getCantidadInicial() % CAPACIDAD_BANDEJA;

        return new CargaResponseDTO(
                carga.getIdCarga(),
                carga.getFusionLote().getNombre(),
                carga.getCategoriaEmbandejado().getCodigo(),
                carga.getCantidadInicial(),
                bandejasCompletas,
                residuo,
                carga.getFechaCarga(),
                carga.getFechaTransferenciaNacedora(),
                carga.getFechaNacimiento(),
                carga.getEstado().name(),
                asignacionesDTO
        );
    }

    private Carga buscarCarga(Long id) {
        return cargaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + id));
    }

	@Override
	public List<CargaResponseDTO> listarCargas() {
		return cargaRepo.findAll().stream()
				.map(carga -> construirResponseCompleto(carga, asignacionRepo.findByCarga(carga)))
				.toList();
	}
}