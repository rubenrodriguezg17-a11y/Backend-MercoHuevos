package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mercohuevos.plantaincubacion.incubacion.repository.ICargaLoteRepository;
import com.mercohuevos.plantaincubacion.recepcion.dto.EditarFusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLotePlantillaDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.LoteOrigenSimpleDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.PlantillaEstadoDTO;
import com.mercohuevos.plantaincubacion.recepcion.model.FusionLoteDetalle;
import com.mercohuevos.plantaincubacion.recepcion.model.FusionLotePlantillaDetalle;
import com.mercohuevos.plantaincubacion.recepcion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.recepcion.repository.IFusionLoteDetalleRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.IFusionLotePlantillaDetalleRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.ILoteOrigenReporteRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.IRecepcionReporteRepository;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.shared.model.FusionLotePlantilla;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLotePlantillaRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLoteRepository;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.recepcion.mapper.IFusionLoteMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FusionLoteImpl implements IFusionLoteService {

    private final IFusionLoteRepository fusionLoteRepo;
    private final IFusionLoteDetalleRepository fusionDetalleRepo;
    private final IRecepcionReporteRepository recepcionRepo;
    private final ICargaLoteRepository cargaLoteRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
    private final IFusionLotePlantillaRepository plantillaRepo;
    private final IFusionLotePlantillaDetalleRepository plantillaDetalleRepo;
    private final IFusionLoteMapper mapper;

    @Override
    @Transactional
    public FusionLoteDTO crear(FusionLoteRequestDTO request) {
        RecepcionReporte recepcion = recepcionRepo.findById(request.idRecepcion())
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + request.idRecepcion()));
        validarRecepcionEditable(recepcion);

        if (fusionLoteRepo.existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
                recepcion, request.idLineaGenetica(), request.nombreFusion())) {
            throw new IllegalArgumentException("Ya existe una fusion con ese nombre en esta linea genetica y recepcion");
        }

        List<FusionLote> fusionesOrigen = cargarYValidarFusionesOrigen(
                request.idsFusionOrigen(), recepcion, request.idLineaGenetica());

        FusionLote nueva = new FusionLote();
        nueva.setRecepcion(recepcion);
        nueva.setIdLineaGenetica(request.idLineaGenetica());
        nueva.setLineaGeneticaNombre(fusionesOrigen.get(0).getLineaGeneticaNombre());
        nueva.setCodigoFusion(request.nombreFusion());
        nueva.setActiva(true);
        aplicarTotales(nueva, fusionesOrigen);

        FusionLote guardada = fusionLoteRepo.save(nueva);

        moverDetallesYAnularOrigen(guardada, fusionesOrigen);

        if (Boolean.TRUE.equals(request.guardarComoRecurrente())) {
            guardarOActualizarPlantilla(guardada);
        }

        return construirResponse(guardada, obtenerLotesOrigen(guardada));
    }

    @Override
    @Transactional
    public FusionLoteDTO editar(Long idFusionLote, EditarFusionLoteRequestDTO request) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        validarRecepcionEditable(fusionLote.getRecepcion());

        if (!fusionLote.getCodigoFusion().equals(request.nombreFusion())
                && fusionLoteRepo.existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
                fusionLote.getRecepcion(), fusionLote.getIdLineaGenetica(), request.nombreFusion())) {
            throw new IllegalArgumentException("Ya existe una fusion con ese nombre en esta linea genetica y recepcion");
        }

        fusionLote.setCodigoFusion(request.nombreFusion());
        FusionLote actualizada = fusionLoteRepo.save(fusionLote);

        return construirResponse(actualizada, obtenerLotesOrigen(actualizada));
    }

    @Override
    @Transactional
    public void anular(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        validarRecepcionEditable(fusionLote.getRecepcion());

        if (cargaLoteRepo.existsByFusionLote(fusionLote)) {
            throw new IllegalStateException("No se puede anular: esta fusion ya tiene una carga registrada");
        }

        fusionLote.setActiva(false);
        fusionLoteRepo.save(fusionLote);
    }

    @Override
    public FusionLoteDTO obtenerPorId(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        return construirResponse(fusionLote, obtenerLotesOrigen(fusionLote));
    }


    @Override
    public List<FusionLoteDTO> listarActivasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcionAndActivaTrue(recepcion).stream()
                .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
                .toList();
    }

    @Override
    public List<FusionLoteDTO> listarAnuladasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcionAndActivaFalse(recepcion).stream()
                .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
                .toList();
    }

    @Override
    public List<FusionLoteDTO> listarTodasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcion(recepcion).stream()
                .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
                .toList();
    }

    // ---------------------- Fusiones recurrentes (plantillas) ----------------------

    @Override
    public List<PlantillaEstadoDTO> obtenerEstadoPlantillas(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));

        List<LoteOrigenReporte> lotesHoy = loteOrigenRepo.findByRecepcion(recepcion);
        java.util.Set<Long> lineasHoy = lotesHoy.stream()
                .map(LoteOrigenReporte::getIdLineaGenetica).collect(Collectors.toSet());
        if (lineasHoy.isEmpty()) return List.of();

        List<FusionLotePlantilla> plantillas = plantillaRepo
                .findByIdLineaGeneticaInAndActivaTrue(new java.util.ArrayList<>(lineasHoy));
        if (plantillas.isEmpty()) return List.of();

        List<FusionLotePlantillaDetalle> detalles = plantillaDetalleRepo.findByPlantillaIn(plantillas);
        Map<Long, List<FusionLotePlantillaDetalle>> detallesPorPlantilla = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getPlantilla().getIdPlantilla()));

        java.util.Set<Long> lotesGranjaHoy = lotesHoy.stream()
                .map(LoteOrigenReporte::getIdLoteGranja).collect(Collectors.toSet());

        List<FusionLote> fusionesHoy = fusionLoteRepo.findByRecepcion(recepcion);

        return plantillas.stream().map(p -> {
            List<FusionLotePlantillaDetalle> esperados =
                    detallesPorPlantilla.getOrDefault(p.getIdPlantilla(), List.of());

            List<String> lotesEsperados = esperados.stream()
                    .map(FusionLotePlantillaDetalle::getCodigoLoteGranja).toList();
            List<String> presentes = esperados.stream()
                    .filter(d -> lotesGranjaHoy.contains(d.getIdLoteGranja()))
                    .map(FusionLotePlantillaDetalle::getCodigoLoteGranja).toList();
            List<String> faltantes = esperados.stream()
                    .filter(d -> !lotesGranjaHoy.contains(d.getIdLoteGranja()))
                    .map(FusionLotePlantillaDetalle::getCodigoLoteGranja).toList();

            boolean aplicada = fusionesHoy.stream()
                    .anyMatch(f -> f.getIdLineaGenetica().equals(p.getIdLineaGenetica())
                            && f.getCodigoFusion().equals(p.getCodigoFusion())
                            && Boolean.TRUE.equals(f.getActiva()));

            return new PlantillaEstadoDTO(
                    p.getIdPlantilla(), p.getCodigoFusion(), p.getIdLineaGenetica(), p.getLineaGeneticaNombre(),
                    lotesEsperados, presentes, faltantes, aplicada);
        }).toList();
    }

    @Override
    public List<FusionLotePlantillaDTO> listarPlantillas() {
        List<FusionLotePlantilla> plantillas = plantillaRepo.findByActivaTrue();
        List<FusionLotePlantillaDetalle> detalles = plantillaDetalleRepo.findByPlantillaIn(plantillas);
        Map<Long, List<String>> codigosPorPlantilla = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getPlantilla().getIdPlantilla(),
                        Collectors.mapping(FusionLotePlantillaDetalle::getCodigoLoteGranja, Collectors.toList())));

        return plantillas.stream()
                .map(p -> new FusionLotePlantillaDTO(
                        p.getIdPlantilla(), p.getIdLineaGenetica(), p.getLineaGeneticaNombre(), p.getCodigoFusion(),
                        p.getActiva(), p.getFechaCreacion(), codigosPorPlantilla.getOrDefault(p.getIdPlantilla(), List.of())))
                .toList();
    }

    @Override
    @Transactional
    public void desactivarPlantilla(Long idPlantilla) {
        FusionLotePlantilla plantilla = plantillaRepo.findById(idPlantilla)
                .orElseThrow(() -> new EntityNotFoundException("Plantilla no encontrada: " + idPlantilla));
        plantilla.setActiva(false);
        plantillaRepo.save(plantilla);
    }

    private void guardarOActualizarPlantilla(FusionLote fusion) {
        List<LoteOrigenReporte> lotesOrigen = obtenerLotesOrigen(fusion);

        FusionLotePlantilla plantilla = plantillaRepo
                .findByIdLineaGeneticaAndCodigoFusion(fusion.getIdLineaGenetica(), fusion.getCodigoFusion())
                .orElseGet(() -> {
                    FusionLotePlantilla nueva = new FusionLotePlantilla();
                    nueva.setIdLineaGenetica(fusion.getIdLineaGenetica());
                    nueva.setLineaGeneticaNombre(fusion.getLineaGeneticaNombre());
                    nueva.setCodigoFusion(fusion.getCodigoFusion());
                    nueva.setFechaCreacion(fusion.getRecepcion().getFechaReporte());
                    nueva.setActiva(true);
                    return nueva;
                });

        plantilla.setActiva(true);
        FusionLotePlantilla plantillaGuardada = plantillaRepo.save(plantilla);

        plantillaDetalleRepo.deleteByPlantilla(plantillaGuardada);

        List<FusionLotePlantillaDetalle> detalles = lotesOrigen.stream()
                .map(l -> {
                    FusionLotePlantillaDetalle d = new FusionLotePlantillaDetalle();
                    d.setPlantilla(plantillaGuardada);
                    d.setIdLoteGranja(l.getIdLoteGranja());
                    d.setCodigoLoteGranja(l.getCodigoLoteGranja());
                    return d;
                }).toList();

        plantillaDetalleRepo.saveAll(detalles);
    }

    // ---------------------- Métodos privados de apoyo ----------------------

    private FusionLote buscarFusion(Long id) {
        return fusionLoteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FusionLote no encontrado: " + id));
    }

    private void validarRecepcionEditable(RecepcionReporte recepcion) {
        if (recepcion.isEmbandejadoConfirmado()) {
            throw new IllegalStateException("No se pueden modificar fusiones: el embandejado de esta recepcion ya fue confirmado");
        }
    }

    private List<FusionLote> cargarYValidarFusionesOrigen(List<Long> idsFusionOrigen, RecepcionReporte recepcion, Long idLineaGenetica) {
        List<FusionLote> fusiones = fusionLoteRepo.findAllById(idsFusionOrigen);

        if (fusiones.size() != idsFusionOrigen.size()) {
            throw new EntityNotFoundException("Una o mas fusiones de origen no existen");
        }

        for (FusionLote fusion : fusiones) {
            if (!fusion.getActiva()) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " ya fue anulada");
            }
            if (!fusion.getRecepcion().getIdRecepcion().equals(recepcion.getIdRecepcion())) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " no pertenece a esta recepcion");
            }
            if (!fusion.getIdLineaGenetica().equals(idLineaGenetica)) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " no pertenece a la linea genetica " + idLineaGenetica);
            }
            if (cargaLoteRepo.existsByFusionLote(fusion)) {
                throw new IllegalStateException("La fusion " + fusion.getCodigoFusion() + " ya tiene una carga registrada, no se puede fusionar");
            }
        }
        return fusiones;
    }

    private void aplicarTotales(FusionLote nueva, List<FusionLote> fusionesOrigen) {
        int totalIncubable = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosIncubablesGuia).sum();
        int totalComercial = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosComercialGuia).sum();
        nueva.setHuevosIncubablesGuia(totalIncubable);
        nueva.setHuevosComercialGuia(totalComercial);
    }

    private void moverDetallesYAnularOrigen(FusionLote nueva, List<FusionLote> fusionesOrigen) {
        List<FusionLoteDetalle> detalles = fusionDetalleRepo.findByFusionLoteIn(fusionesOrigen);
        detalles.forEach(d -> d.setFusionLote(nueva));
        fusionDetalleRepo.saveAll(detalles);

        fusionesOrigen.forEach(f -> f.setActiva(false));
        fusionLoteRepo.saveAll(fusionesOrigen);
    }

    private List<LoteOrigenReporte> obtenerLotesOrigen(FusionLote fusionLote) {
        return fusionDetalleRepo.findByFusionLote(fusionLote).stream()
                .map(FusionLoteDetalle::getLoteOrigenReporte)
                .toList();
    }

    private FusionLoteDTO construirResponse(FusionLote fusionLote, List<LoteOrigenReporte> lotesOrigen) {
        List<LoteOrigenSimpleDTO> lotesDTO = lotesOrigen.stream()
                .map(l -> new LoteOrigenSimpleDTO(l.getIdLoteGranja(), l.getCodigoLoteGranja()))
                .toList();

        return new FusionLoteDTO(
                fusionLote.getIdFusionLote(),
                fusionLote.getIdLineaGenetica(),
                fusionLote.getLineaGeneticaNombre(),
                fusionLote.getCodigoFusion(),
                fusionLote.getHuevosIncubablesGuia(),
                fusionLote.getHuevosComercialGuia()
        );
    }
}