package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mercohuevos.plantaincubacion.incubacion.dto.*;
import com.mercohuevos.plantaincubacion.incubacion.model.*;
import com.mercohuevos.plantaincubacion.incubacion.repository.*;
import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.shared.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IMaquinaRepository;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargaImpl implements ICargaService {

    private static final int CAPACIDAD_BANDEJA = 96;

    private final ICargaRepository cargaRepo;
    private final ICargaLoteRepository cargaLoteRepo;
    private final StockIncubableMovimientoService stockMovimiento;
    private final ICategoriaCargaRepository categoriaCargaRepo;
    private final IAsignacionCargaMaquinaRepository asignacionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final IMaquinaRepository maquinaRepo;
    private final IDetalleMirajeLoteRepository detalleMirajeRepo;
    private final IMirajeRepository mirajeRepo;

    @Override
    @Transactional
    public CargaResponseDTO crear(CargaRequestDTO request) {

        List<LineaGeneticaCargaResponseDTO> lineasDTO = new ArrayList<>();

        for (LineaGeneticaCargaRequestDTO lineaReq : request.lineasGeneticas()) {

            if (lineaReq.lotesFusion().isEmpty()) {
                throw new IllegalArgumentException("La linea genetica " + lineaReq.idLineaGenetica() + " no tiene lotes");
            }

            List<LoteFusionCargaResponseDTO> lotesDTO = new ArrayList<>();
            Carga carga = null; // se crea una sola vez por linea, con el nombre del primer fusionLote validado

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

                if (carga == null) {
                    carga = obtenerOCrearCarga(fusionLote.getIdLineaGenetica(), fusionLote.getLineaGeneticaNombre(), request.fechaCarga());
                }

                CargaLote cargaLote = obtenerOCrearCargaLote(carga, fusionLote);
                List<CategoriaCargaResponseDTO> categoriasDTO = new ArrayList<>();

                for (CategoriaCargaRequestDTO catReq : loteReq.categoriasCargadas()) {
                    CategoriaEmbandejado categoria = categoriaRepo.findById(catReq.idCategoriaEmbandejado())
                            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + catReq.idCategoriaEmbandejado()));

                    Maquina maquina = maquinaRepo.findById(catReq.idMaquina())
                            .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + catReq.idMaquina()));

                    if (maquina.getTipo() != TipoMaquina.INCUBADORA) {
                        throw new IllegalArgumentException("La maquina " + maquina.getNumero() + " no es una incubadora");
                    }

                    StockIncubable stock = stockMovimiento.obtenerOCrear(fusionLote, categoria, request.fechaCarga());
                    if (stock.getStockActual() < catReq.cantidadCargada()) {
                        throw new IllegalArgumentException(
                                "Stock insuficiente en " + fusionLote.getCodigoFusion() + " / " + categoria.getCodigoCategoria() +
                                        ". Disponible: " + stock.getStockActual() + ", solicitado: " + catReq.cantidadCargada());
                    }

                    validarCapacidadMaquina(maquina, catReq.cantidadCargada());

                    CategoriaCarga cc = registrarCategoriaCarga(carga, fusionLote, categoria, maquina, catReq.cantidadCargada());
                    acumularAsignacionMaquina(carga, maquina, catReq.cantidadCargada());

                    stock.setCargaIncubadora(stock.getCargaIncubadora() + catReq.cantidadCargada());
                    stockMovimiento.recalcularStockActual(stock);
                    stockMovimiento.guardar(stock);
                    stockMovimiento.recalcularEnCascadaDesde(stock);

                    cargaLote.setCantidadInicial(cargaLote.getCantidadInicial() + catReq.cantidadCargada());
                    carga.setCantidadInicial(carga.getCantidadInicial() + catReq.cantidadCargada());

                    categoriasDTO.add(new CategoriaCargaResponseDTO(
                            categoria.getIdCategoriaEmbandejado(),
                            categoria.getCodigoCategoria(),
                            cc.getCantidadInicial(),
                            maquina.getIdMaquina(),
                            String.valueOf(maquina.getNumero())
                    ));
                }

                cargaLoteRepo.save(cargaLote);

                int totalLote = categoriasDTO.stream().mapToInt(CategoriaCargaResponseDTO::cantidadCargada).sum();
                lotesDTO.add(new LoteFusionCargaResponseDTO(
                        fusionLote.getIdFusionLote(), fusionLote.getCodigoFusion(), totalLote, categoriasDTO));
            }

            cargaRepo.save(carga);

            int totalLinea = lotesDTO.stream().mapToInt(LoteFusionCargaResponseDTO::totalCargadoLote).sum();
            lineasDTO.add(new LineaGeneticaCargaResponseDTO(
                    lineaReq.idLineaGenetica(), carga.getLineaGeneticaNombre(), totalLinea, lotesDTO));
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
        return cargaRepo.findByEstadoAndIdLineaGenetica(EstadoCarga.EN_INCUBACION, idLineaGenetica).stream()
                .map(carga -> new CargaDisponibleMirajeDTO(
                        carga.getIdCarga(),
                        carga.getCantidadInicial(),
                        detalleMirajeRepo.sumHuevosInfertilesPorCarga(carga),                        carga.getFechaCarga()
                ))
                .toList();
    }

    @Override
    public List<CargaLoteResumenDTO> listarLotesPorCarga(Long idCarga) {
        Carga carga = buscarCarga(idCarga);
        return cargaLoteRepo.findByCarga(carga).stream()
                .map(cl -> new CargaLoteResumenDTO(
                        cl.getIdCargaLote(),
                        cl.getFusionLote().getIdFusionLote(),
                        cl.getFusionLote().getCodigoFusion(),
                        cl.getCantidadInicial()
                ))
                .toList();
    }

    @Override
    public CargaLoteResumenDTO obtenerLote(Long idCargaLote) {
        CargaLote cl = cargaLoteRepo.findById(idCargaLote)
                .orElseThrow(() -> new EntityNotFoundException("Lote Cargado no encontrado: " + idCargaLote));

        return new CargaLoteResumenDTO(
                cl.getIdCargaLote(),
                cl.getFusionLote().getIdFusionLote(),
                cl.getFusionLote().getCodigoFusion(),
                cl.getCantidadInicial()
        );
    }

    @Override
    @Transactional
    public void cambiarEstado(Long idCarga, EstadoCarga nuevoEstado) {
        Carga carga = buscarCarga(idCarga);
        carga.setEstado(nuevoEstado);
        cargaRepo.save(carga);
    }

    @Override
    public List<CargaDetalleResponseDTO> listarCargas() {
        return cargaRepo.findAll().stream()
                .map(carga -> construirResponseCompleto(
                        carga, categoriaCargaRepo.findByCarga(carga), asignacionRepo.findByCarga(carga)))
                .toList();
    }

    @Override
    @Transactional
    public CargaResponseDTO editar(Long id, CargaRequestDTO request) {
        Carga carga = buscarCarga(id);

        if (mirajeRepo.existsByCarga(carga)) {
            throw new IllegalStateException("No se puede editar: esta carga ya tiene miraje registrado");
        }
        if (request.lineasGeneticas().size() != 1) {
            throw new IllegalArgumentException(
                    "La edicion de una carga solo admite la linea genetica ya asociada a ella");
        }
        LineaGeneticaCargaRequestDTO lineaReq = request.lineasGeneticas().get(0);
        if (!lineaReq.idLineaGenetica().equals(carga.getIdLineaGenetica())) {
            throw new IllegalArgumentException(
                    "La linea genetica del request no coincide con la de la carga " + id);
        }

        List<CategoriaCarga> categoriasPrevias = categoriaCargaRepo.findByCarga(carga);
        revertirStockYAsociaciones(carga, categoriasPrevias);

        carga.setCantidadInicial(0);
        carga.setFechaCarga(request.fechaCarga());
        carga.setFechaTransferenciaNacedora(request.fechaCarga().plusDays(18));
        carga.setFechaNacimiento(request.fechaCarga().plusDays(21));

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

            CargaLote cargaLote = obtenerOCrearCargaLote(carga, fusionLote);
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

                CategoriaCarga cc = registrarCategoriaCarga(carga, fusionLote, categoria, maquina, catReq.cantidadCargada());
                acumularAsignacionMaquina(carga, maquina, catReq.cantidadCargada());

                stock.setCargaIncubadora(stock.getCargaIncubadora() + catReq.cantidadCargada());
                stockMovimiento.recalcularStockActual(stock);
                stockMovimiento.guardar(stock);

                cargaLote.setCantidadInicial(cargaLote.getCantidadInicial() + catReq.cantidadCargada());
                carga.setCantidadInicial(carga.getCantidadInicial() + catReq.cantidadCargada());

                categoriasDTO.add(new CategoriaCargaResponseDTO(
                        categoria.getIdCategoriaEmbandejado(),
                        categoria.getCodigoCategoria(),
                        cc.getCantidadInicial(),
                        maquina.getIdMaquina(),
                        String.valueOf(maquina.getNumero())
                ));
            }

            cargaLoteRepo.save(cargaLote);

            int totalLote = categoriasDTO.stream().mapToInt(CategoriaCargaResponseDTO::cantidadCargada).sum();
            lotesDTO.add(new LoteFusionCargaResponseDTO(
                    fusionLote.getIdFusionLote(), fusionLote.getCodigoFusion(), totalLote, categoriasDTO));
        }

        cargaRepo.save(carga);

        int totalLinea = lotesDTO.stream().mapToInt(LoteFusionCargaResponseDTO::totalCargadoLote).sum();
        return new CargaResponseDTO(carga.getFechaCarga(), totalLinea, List.of(
                new LineaGeneticaCargaResponseDTO(carga.getIdLineaGenetica(), carga.getLineaGeneticaNombre(), totalLinea, lotesDTO)));
    }

    @Override
    @Transactional
    public void anular(Long id) {
        Carga carga = buscarCarga(id);

        if (mirajeRepo.existsByCarga(carga)) {
            throw new IllegalStateException("No se puede anular: esta carga ya tiene miraje registrado");
        }
        if (carga.getEstado() == EstadoCarga.ANULADA) {
            throw new IllegalStateException("Esta carga ya fue anulada anteriormente");
        }

        List<CategoriaCarga> categorias = categoriaCargaRepo.findByCarga(carga);
        revertirStockYAsociaciones(carga, categorias);

        carga.setCantidadInicial(0);
        carga.setEstado(EstadoCarga.ANULADA);
        cargaRepo.save(carga);
    }

    private void revertirStockYAsociaciones(Carga carga, List<CategoriaCarga> categorias) {
        for (CategoriaCarga cc : categorias) {
            StockIncubable stock = stockMovimiento.obtenerOCrearStockDeHoy(cc.getFusionLote(), cc.getCategoriaEmbandejado());
            stock.setCargaIncubadora(stock.getCargaIncubadora() - cc.getCantidadInicial());
            stockMovimiento.recalcularStockActual(stock);
            stockMovimiento.guardar(stock);
        }
        asignacionRepo.deleteAll(asignacionRepo.findByCarga(carga));
        cargaLoteRepo.deleteAll(cargaLoteRepo.findByCarga(carga));
        categoriaCargaRepo.deleteAll(categorias);
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private Carga obtenerOCrearCarga(Long idLineaGenetica, String nombreLinea, LocalDate fechaCarga) {
        Optional<Carga> existente = cargaRepo.findByIdLineaGeneticaAndFechaCargaAndEstado(
                idLineaGenetica, fechaCarga, EstadoCarga.EN_INCUBACION);

        if (existente.isPresent()) {
            return existente.get();
        }

        Carga nueva = new Carga();
        nueva.setIdLineaGenetica(idLineaGenetica);
        nueva.setLineaGeneticaNombre(nombreLinea);
        nueva.setCantidadInicial(0);
        nueva.setFechaCarga(fechaCarga);
        nueva.setFechaTransferenciaNacedora(fechaCarga.plusDays(18));
        nueva.setFechaNacimiento(fechaCarga.plusDays(21));
        nueva.setEstado(EstadoCarga.EN_INCUBACION);
        return cargaRepo.save(nueva);
    }

    private CargaLote obtenerOCrearCargaLote(Carga carga, FusionLote fusionLote) {
        return cargaLoteRepo.findByCargaAndFusionLote(carga, fusionLote)
                .orElseGet(() -> {
                    CargaLote nueva = new CargaLote();
                    nueva.setCarga(carga);
                    nueva.setFusionLote(fusionLote);
                    nueva.setCantidadInicial(0);
                    return nueva;
                });
    }

    private CategoriaCarga registrarCategoriaCarga(
            Carga carga, FusionLote fusionLote, CategoriaEmbandejado categoria, Maquina maquina, Integer cantidad) {

        CategoriaCarga cc = categoriaCargaRepo.findByCargaAndFusionLoteAndCategoriaEmbandejado(carga, fusionLote, categoria)
                .orElseGet(() -> {
                    CategoriaCarga nueva = new CategoriaCarga();
                    nueva.setCarga(carga);
                    nueva.setFusionLote(fusionLote);
                    nueva.setCategoriaEmbandejado(categoria);
                    nueva.setMaquina(maquina);
                    nueva.setCantidadInicial(0);
                    return nueva;
                });

        if (!cc.getMaquina().getIdMaquina().equals(maquina.getIdMaquina())) {
            throw new IllegalArgumentException(
                    "La categoria " + categoria.getCodigoCategoria() + " del lote " + fusionLote.getCodigoFusion() +
                            " ya fue cargada en otra maquina (" + cc.getMaquina().getNumero() + "); no se puede repartir entre dos maquinas");
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
                    nueva.setFechaIngreso(carga.getFechaCarga());
                    return nueva;
                });
        asignacion.setCantidadAsignada(asignacion.getCantidadAsignada() + cantidad);
        asignacionRepo.save(asignacion);
    }

    private CargaDetalleResponseDTO construirResponseCompleto(
            Carga carga, List<CategoriaCarga> categoriasCarga, List<AsignacionCargaMaquina> asignaciones) {

        var porLote = categoriasCarga.stream()
                .collect(java.util.stream.Collectors.groupingBy(CategoriaCarga::getFusionLote));

        List<LoteFusionCargaResponseDTO> lotesDTO = porLote.entrySet().stream()
                .map(e -> {
                    FusionLote fl = e.getKey();
                    List<CategoriaCargaResponseDTO> catsDTO = e.getValue().stream()
                            .map(cc -> new CategoriaCargaResponseDTO(
                                    cc.getCategoriaEmbandejado().getIdCategoriaEmbandejado(),
                                    cc.getCategoriaEmbandejado().getCodigoCategoria(),
                                    cc.getCantidadInicial(),
                                    cc.getMaquina().getIdMaquina(),
                                    String.valueOf(cc.getMaquina().getNumero())))
                            .toList();
                    int totalLote = catsDTO.stream().mapToInt(CategoriaCargaResponseDTO::cantidadCargada).sum();
                    return new LoteFusionCargaResponseDTO(fl.getIdFusionLote(), fl.getCodigoFusion(), totalLote, catsDTO);
                })
                .toList();

        List<AsignacionMaquinaResponseDTO> asignacionesDTO = asignaciones.stream()
                .map(a -> new AsignacionMaquinaResponseDTO(a.getMaquina().getNumero(), a.getCantidadAsignada()))
                .toList();

        int bandejasCompletas = carga.getCantidadInicial() / CAPACIDAD_BANDEJA;
        int residuo = carga.getCantidadInicial() % CAPACIDAD_BANDEJA;

        return new CargaDetalleResponseDTO(
                carga.getIdCarga(),
                carga.getLineaGeneticaNombre(),
                lotesDTO,
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