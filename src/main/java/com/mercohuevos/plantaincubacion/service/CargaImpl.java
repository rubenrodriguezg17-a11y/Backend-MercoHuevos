package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.AsignacionMaquinaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.AsignacionMaquinaResponseDTO;
import com.mercohuevos.plantaincubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.CargaResponseDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaCargaResponseDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.mapper.IAsignacionCargaMaquinaMapper;
import com.mercohuevos.plantaincubacion.model.AsignacionCargaMaquina;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.CategoriaCarga;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.Maquina;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.IAsignacionCargaMaquinaRepository;
import com.mercohuevos.plantaincubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.repository.ICategoriaCargaRepository;
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
    private final ICategoriaCargaRepository categoriaCargaRepo;
    private final IAsignacionCargaMaquinaRepository asignacionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final IMaquinaRepository maquinaRepo;
    private final IStockIncubableRepository stockRepo;
    private final IAsignacionCargaMaquinaMapper asignacionMapper;

    @Override
    @Transactional
    public CargaResponseDTO crear(CargaRequestDTO request) {

        FusionLote fusionLote = fusionLoteRepo.findById(request.idFusionLote())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fusion de lote no encontrada: " + request.idFusionLote()));

        record CategoriaValidada(CategoriaEmbandejado categoria, Integer cantidad, StockIncubable stock) {}

        List<CategoriaValidada> categoriasValidadas = request.categoriasEmbandejado().stream()
                .map(c -> {
                    CategoriaEmbandejado categoria = categoriaRepo.findById(c.idCategoriaEmbandejado())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Categoria de embandejado no encontrada: " + c.idCategoriaEmbandejado()));
                    StockIncubable stock = validarYObtenerStockDisponible(
                            fusionLote, categoria, request.fechaCarga(), c.cantidadInicial());
                    return new CategoriaValidada(categoria, c.cantidadInicial(), stock);
                })
                .toList();

        int totalNuevo = categoriasValidadas.stream()
                .mapToInt(CategoriaValidada::cantidad)
                .sum();

        int sumaMaquinasNueva = request.maquinas().stream()
                .mapToInt(AsignacionMaquinaRequestDTO::cantidad)
                .sum();

        if (sumaMaquinasNueva != totalNuevo) {
            throw new IllegalArgumentException(
                    "La suma de las cantidades asignadas a maquinas (" + sumaMaquinasNueva +
                    ") debe ser igual a la suma de las categorias enviadas (" + totalNuevo + ")");
        }

        Optional<Carga> cargaExistente = cargaRepo.findByFusionLoteAndFechaCargaAndEstado(
                fusionLote, request.fechaCarga(), EstadoCarga.EN_INCUBACION);

        final Carga carga;
        if (cargaExistente.isPresent()) {
            Carga cExistente = cargaExistente.get();
            cExistente.setCantidadInicial(cExistente.getCantidadInicial() + totalNuevo);
            carga = cargaRepo.save(cExistente);
        } else {
            Carga cNueva = new Carga();
            cNueva.setFusionLote(fusionLote);
            cNueva.setCantidadInicial(totalNuevo);
            cNueva.setFechaCarga(request.fechaCarga());
            cNueva.setFechaTransferenciaNacedora(request.fechaCarga().plusDays(18));
            cNueva.setFechaNacimiento(request.fechaCarga().plusDays(21));
            cNueva.setEstado(EstadoCarga.EN_INCUBACION);
            carga = cargaRepo.save(cNueva);
        }

        for (CategoriaValidada cv : categoriasValidadas) {
            CategoriaCarga cc = categoriaCargaRepo.findByCargaAndCategoriaEmbandejado(carga, cv.categoria())
                    .orElseGet(() -> {
                        CategoriaCarga nueva = new CategoriaCarga();
                        nueva.setCarga(carga);
                        nueva.setCategoriaEmbandejado(cv.categoria());
                        nueva.setCantidadInicial(0);
                        return nueva;
                    });
            cc.setCantidadInicial(cc.getCantidadInicial() + cv.cantidad());
            categoriaCargaRepo.save(cc);
        }

        for (AsignacionMaquinaRequestDTO asignacionReq : request.maquinas()) {
            construirOAcumularAsignacion(asignacionReq, carga);
        }

        categoriasValidadas.forEach(cv -> descontarStock(cv.stock(), cv.cantidad()));

        List<CategoriaCarga> categoriasCarga = categoriaCargaRepo.findByCarga(carga);
        List<AsignacionCargaMaquina> asignaciones = asignacionRepo.findByCarga(carga);

        return construirResponseCompleto(carga, categoriasCarga, asignaciones);
    }

    @Override
    @Transactional
    public CargaResponseDTO obtenerPorId(Long id) {
        Carga carga = buscarCarga(id);
        List<CategoriaCarga> categoriasCarga = categoriaCargaRepo.findByCarga(carga);
        List<AsignacionCargaMaquina> asignaciones = asignacionRepo.findByCarga(carga);
        return construirResponseCompleto(carga, categoriasCarga, asignaciones);
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private StockIncubable validarYObtenerStockDisponible(
            FusionLote fusionLote, CategoriaEmbandejado categoria, LocalDate fecha, Integer cantidadSolicitada) {

        StockIncubable stock = stockRepo
                .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(
                        fusionLote, categoria, fecha.plusDays(1))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay stock incubable registrado para la categoria " + categoria.getCodigoCategoria()));

        if (stock.getStockActual() < cantidadSolicitada) {
            throw new IllegalArgumentException(
                    "Stock insuficiente en categoria " + categoria.getCodigoCategoria() +
                    ". Disponible: " + stock.getStockActual() + ", solicitado: " + cantidadSolicitada);
        }

        return stock;
    }

    private void descontarStock(StockIncubable stock, Integer cantidad) {
        stock.setCargaIncubadora(stock.getCargaIncubadora() + cantidad);
        stock.setStockActual(stock.getStockActual() - cantidad);
        stockRepo.save(stock);
    }

    private void construirOAcumularAsignacion(AsignacionMaquinaRequestDTO asignacionReq, Carga carga) {

        Maquina maquina = maquinaRepo.findById(asignacionReq.idMaquina())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Maquina no encontrada: " + asignacionReq.idMaquina()));

        if (maquina.getTipo() != TipoMaquina.INCUBADORA) {
            throw new IllegalArgumentException(
                    "La maquina " + maquina.getNumero() + " no es una incubadora");
        }

        int yaAsignado = asignacionRepo.sumAsignadoActivoPorMaquina(
                maquina, FaseAsignacion.INCUBACION, EstadoCarga.FINALIZADA);
        int disponible = maquina.getCapacidadMaxima() - yaAsignado;

        if (asignacionReq.cantidad() > disponible) {
            throw new IllegalArgumentException(
                    "Capacidad insuficiente en incubadora " + maquina.getNumero() +
                    ". Disponible: " + disponible + ", solicitado: " + asignacionReq.cantidad());
        }

        AsignacionCargaMaquina asignacion = asignacionRepo
                .findByCargaAndMaquinaAndFase(carga, maquina, FaseAsignacion.INCUBACION)
                .orElseGet(() -> {
                    AsignacionCargaMaquina nueva = new AsignacionCargaMaquina();
                    nueva.setCarga(carga);
                    nueva.setMaquina(maquina);
                    nueva.setFase(FaseAsignacion.INCUBACION);
                    nueva.setCantidadAsignada(0);
                    return nueva;
                });

        asignacion.setCantidadAsignada(asignacion.getCantidadAsignada() + asignacionReq.cantidad());
        asignacionRepo.save(asignacion);
    }

    private CargaResponseDTO construirResponseCompleto(
            Carga carga, List<CategoriaCarga> categoriasCarga, List<AsignacionCargaMaquina> asignaciones) {

        List<CategoriaCargaResponseDTO> categoriasDTO = categoriasCarga.stream()
                .map(cc -> new CategoriaCargaResponseDTO(
                        cc.getCategoriaEmbandejado().getCodigoCategoria(), cc.getCantidadInicial()))
                .toList();

        List<AsignacionMaquinaResponseDTO> asignacionesDTO = asignaciones.stream()
                .map(asignacionMapper::toResponseDTO)
                .toList();

        int bandejasCompletas = carga.getCantidadInicial() / CAPACIDAD_BANDEJA;
        int residuo = carga.getCantidadInicial() % CAPACIDAD_BANDEJA;

        return new CargaResponseDTO(
                carga.getIdCarga(),
                carga.getFusionLote().getCodigoFusion(),
                categoriasDTO,
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
                .map(carga -> construirResponseCompleto(
                        carga, categoriaCargaRepo.findByCarga(carga), asignacionRepo.findByCarga(carga)))
                .toList();
    }
}