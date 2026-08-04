package com.mercohuevos.plantaincubacion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.*;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.model.*;
import com.mercohuevos.plantaincubacion.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargaImpl implements ICargaService {

    private static final int CAPACIDAD_BANDEJA = 96;

    private final ICargaRepository cargaRepo;
    private final StockIncubableMovimientoService stockMovimiento;
    private final ICategoriaCargaRepository categoriaCargaRepo;
    private final IAsignacionCargaMaquinaRepository asignacionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final IMaquinaRepository maquinaRepo;
    private final IRegistroMirajeRepository mirajeRepo;
    
    @Override
    @Transactional
    public CargaResponseDTO crear(CargaRequestDTO request) {

        List<LineaGeneticaCargaResponseDTO> lineasDTO = new ArrayList<>();

        for (LineaGeneticaCargaRequestDTO lineaReq : request.lineasGeneticas()) {
            List<LoteFusionCargaResponseDTO> lotesDTO = new ArrayList<>();

            for (LoteFusionCargaRequestDTO loteReq : lineaReq.lotesFusion()) {
                FusionLote fusionLote = fusionLoteRepo.findById(loteReq.idFusionLote())
                    .orElseThrow(() -> new EntityNotFoundException("FusionLote no encontrado: " + loteReq.idFusionLote()));

                if (!fusionLote.getActiva()) {
                    throw new IllegalArgumentException("La fusion " + fusionLote.getCodigoFusion() + " esta anulada");
                }
                if (!fusionLote.getIdLineaGenetica().equals(lineaReq.idLineaGenetica())) {
                    throw new IllegalArgumentException(
                        "El fusionLote " + loteReq.idFusionLote() + " no pertenece a la linea genetica " + lineaReq.idLineaGenetica());
                }

                Carga carga = obtenerOCrearCarga(fusionLote, request.fechaCarga());
                List<CategoriaCargaResponseDTO> categoriasDTO = new ArrayList<>();

                for (CategoriaCargaRequestDTO catReq : loteReq.categoriasCargadas()) {
                    CategoriaEmbandejado categoria = categoriaRepo.findById(catReq.idCategoriaEmbandejado())
                        .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + catReq.idCategoriaEmbandejado()));

                    Maquina maquina = maquinaRepo.findById(catReq.idMaquina())
                        .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + catReq.idMaquina()));

                    if (maquina.getTipo() != TipoMaquina.INCUBADORA) {
                        throw new IllegalArgumentException("La maquina " + maquina.getNumero() + " no es una incubadora");
                    }

                    StockIncubable stock = stockMovimiento.obtenerOCrearStockDeHoy(fusionLote, categoria);
                    if (stock.getStockActual() < catReq.cantidadCargada()) {
                        throw new IllegalArgumentException(
                            "Stock insuficiente en " + fusionLote.getCodigoFusion() + " / " + categoria.getCodigoCategoria() +
                            ". Disponible: " + stock.getStockActual() + ", solicitado: " + catReq.cantidadCargada());
                    }

                    validarCapacidadMaquina(maquina, catReq.cantidadCargada());

                    CategoriaCarga cc = registrarCategoriaCarga(carga, categoria, maquina, catReq.cantidadCargada());
                    acumularAsignacionMaquina(carga, maquina, catReq.cantidadCargada());

                    stock.setCargaIncubadora(stock.getCargaIncubadora() + catReq.cantidadCargada());
                    stockMovimiento.recalcularStockActual(stock);
                    stockMovimiento.guardar(stock);

                    carga.setCantidadInicial(carga.getCantidadInicial() + catReq.cantidadCargada());

                    categoriasDTO.add(new CategoriaCargaResponseDTO(
                            categoria.getIdCategoriaEmbandejado(), 
                            categoria.getCodigoCategoria(),
                            cc.getCantidadInicial(), 
                            maquina.getIdMaquina(), 
                            String.valueOf(maquina.getNumero())
                        ));
                    }

                cargaRepo.save(carga);

                int totalLote = categoriasDTO.stream().mapToInt(CategoriaCargaResponseDTO::cantidadCargada).sum();
                lotesDTO.add(new LoteFusionCargaResponseDTO(
                    fusionLote.getIdFusionLote(), fusionLote.getCodigoFusion(), totalLote, categoriasDTO));
            }

            int totalLinea = lotesDTO.stream().mapToInt(LoteFusionCargaResponseDTO::totalCargadoLote).sum();
            lineasDTO.add(new LineaGeneticaCargaResponseDTO(
                lineaReq.idLineaGenetica(), lotesDTO.get(0) != null ? obtenerNombreLinea(lineaReq.idLineaGenetica(), lotesDTO) : "",
                totalLinea, lotesDTO));
        }

        int totalGlobal = lineasDTO.stream().mapToInt(LineaGeneticaCargaResponseDTO::totalCargadoLinea).sum();
        return new CargaResponseDTO(request.fechaCarga(), totalGlobal, lineasDTO);
    }

    @Override
    public CargaDetalleResponseDTO obtenerPorId(Long id) {
        Carga carga = buscarCarga(id);
        List<CategoriaCarga> categoriasCarga = categoriaCargaRepo.findByCarga(carga);
        List<AsignacionCargaMaquina> asignaciones = asignacionRepo.findByCarga(carga);
        return construirResponseCompleto(carga, categoriasCarga, asignaciones);
    }
    
    @Override
    public List<CargaDisponibleMirajeDTO> listarDisponiblesParaMirajePorLinea(Long idLineaGenetica) {
        return cargaRepo.findByEstadoAndFusionLote_IdLineaGenetica(EstadoCarga.EN_INCUBACION, idLineaGenetica).stream()
            .map(carga -> new CargaDisponibleMirajeDTO(
                carga.getIdCarga(),
                carga.getFusionLote().getIdFusionLote(),
                carga.getFusionLote().getCodigoFusion(),
                carga.getCantidadInicial(),
                mirajeRepo.sumNoFecundadoPorCarga(carga),
                carga.getFechaCarga()
            ))
            .toList();
    }
    @Override
    public List<CargaDetalleResponseDTO> listarCargas() {
        return cargaRepo.findAll().stream()
            .map(carga -> construirResponseCompleto(
                carga, categoriaCargaRepo.findByCarga(carga), asignacionRepo.findByCarga(carga)))
            .toList();
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private Carga obtenerOCrearCarga(FusionLote fusionLote, java.time.LocalDate fechaCarga) {
        Optional<Carga> existente = cargaRepo.findByFusionLoteAndFechaCargaAndEstado(
            fusionLote, fechaCarga, EstadoCarga.EN_INCUBACION);

        if (existente.isPresent()) {
            return existente.get();
        }

        Carga nueva = new Carga();
        nueva.setFusionLote(fusionLote);
        nueva.setCantidadInicial(0);
        nueva.setFechaCarga(fechaCarga);
        nueva.setFechaTransferenciaNacedora(fechaCarga.plusDays(18));
        nueva.setFechaNacimiento(fechaCarga.plusDays(21));
        nueva.setEstado(EstadoCarga.EN_INCUBACION);
        return cargaRepo.save(nueva);
    }

    private CategoriaCarga registrarCategoriaCarga(Carga carga, CategoriaEmbandejado categoria, Maquina maquina, Integer cantidad) {
        CategoriaCarga cc = categoriaCargaRepo.findByCargaAndCategoriaEmbandejado(carga, categoria)
            .orElseGet(() -> {
                CategoriaCarga nueva = new CategoriaCarga();
                nueva.setCarga(carga);
                nueva.setCategoriaEmbandejado(categoria);
                nueva.setMaquina(maquina);
                nueva.setCantidadInicial(0);
                return nueva;
            });

        if (!cc.getMaquina().getIdMaquina().equals(maquina.getIdMaquina())) {
            throw new IllegalArgumentException(
                "La categoria " + categoria.getCodigoCategoria() + " de este lote ya fue cargada en otra maquina (" +
                cc.getMaquina().getNumero() + "); no se puede repartir entre dos maquinas");
        }

        cc.setCantidadInicial(cc.getCantidadInicial() + cantidad);
        return categoriaCargaRepo.save(cc);
    }

    private void validarCapacidadMaquina(Maquina maquina, Integer cantidad) {
        int yaAsignado = asignacionRepo.sumAsignadoActivoPorMaquina(
            maquina, FaseAsignacion.INCUBACION, EstadoCarga.FINALIZADA);
        int disponible = maquina.getCapacidadMaxima() - yaAsignado;

        if (cantidad > disponible) {
            throw new IllegalArgumentException(
                "Capacidad insuficiente en incubadora " + maquina.getNumero() +
                ". Disponible: " + disponible + ", solicitado: " + cantidad);
        }
    }

    private void acumularAsignacionMaquina(Carga carga, Maquina maquina, Integer cantidad) {
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
        asignacion.setCantidadAsignada(asignacion.getCantidadAsignada() + cantidad);
        asignacionRepo.save(asignacion);
    }

    private String obtenerNombreLinea(Long idLineaGenetica, List<LoteFusionCargaResponseDTO> lotesDTO) {
        // el nombre de linea no viaja en LoteFusionCargaResponseDTO; lo resolvemos via el primer fusionLote consultado
        return fusionLoteRepo.findById(lotesDTO.get(0).idFusionLote())
            .map(FusionLote::getLineaGeneticaNombre)
            .orElse("");
    }

    private CargaDetalleResponseDTO construirResponseCompleto(
            Carga carga, List<CategoriaCarga> categoriasCarga, List<AsignacionCargaMaquina> asignaciones) {

        List<CategoriaCargaResponseDTO> categoriasDTO = categoriasCarga.stream()
                .map(cc -> new CategoriaCargaResponseDTO(
                        cc.getCategoriaEmbandejado().getIdCategoriaEmbandejado(),
                        cc.getCategoriaEmbandejado().getCodigoCategoria(), 
                        cc.getCantidadInicial(),
                        cc.getMaquina().getIdMaquina(),
                        String.valueOf(cc.getMaquina().getNumero())
                ))
                .toList();

        List<AsignacionMaquinaResponseDTO> asignacionesDTO = asignaciones.stream()
                .map(a -> new AsignacionMaquinaResponseDTO(a.getMaquina().getNumero(), a.getCantidadAsignada()))
                .toList();

        int bandejasCompletas = carga.getCantidadInicial() / CAPACIDAD_BANDEJA;
        int residuo = carga.getCantidadInicial() % CAPACIDAD_BANDEJA;

        return new CargaDetalleResponseDTO(
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
}