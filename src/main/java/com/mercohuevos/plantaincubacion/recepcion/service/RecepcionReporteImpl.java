package com.mercohuevos.plantaincubacion.recepcion.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import com.mercohuevos.plantaincubacion.shared.model.FusionLotePlantilla;
import com.mercohuevos.plantaincubacion.recepcion.model.FusionLotePlantillaDetalle;
import com.mercohuevos.plantaincubacion.recepcion.repository.IFusionLotePlantillaDetalleRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLotePlantillaRepository;


import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.incubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.incubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.incubacion.service.IConsumoHuevoService;
import com.mercohuevos.plantaincubacion.recepcion.dto.*;
import com.mercohuevos.plantaincubacion.recepcion.model.ConteoComercialLinea;
import com.mercohuevos.plantaincubacion.recepcion.repository.IConteoComercialLineaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mercohuevos.common.dto.ConteoTipoHuevoEventDTO;
import com.mercohuevos.common.dto.DetalleLoteEventDTO;
import com.mercohuevos.common.dto.ReporteRecibidoConfirmadoDTO;
import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.common.event.ReporteRecibidoConfirmadoEvent;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.recepcion.model.FusionLoteDetalle;
import com.mercohuevos.plantaincubacion.recepcion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.recepcion.repository.IFusionLoteDetalleRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.ILoteOrigenReporteRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.IRecepcionReporteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecepcionReporteImpl implements IRecepcionReporteService {

    private final IRecepcionReporteRepository recepcionRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final IFusionLoteDetalleRepository fusionDetalleRepo;
    private final IFusionLotePlantillaRepository plantillaRepo;
    private final IFusionLotePlantillaDetalleRepository plantillaDetalleRepo;
    private final IClasificacionTipoHuevoService clasificacionService;
    private final ApplicationEventPublisher eventPublisher;
    private final IConsumoHuevoService consumoHuevoService;
    private final IConteoComercialLineaRepository conteoComercialRepo;
    private final IConsumoHuevoRepository consumoHuevoRepo;


    @Override
    @Transactional
    public void procesarReporteRecibido(ReporteTrasladoEventDTO reporteEvento) {

        if (recepcionRepo.existsByIdReporteGranja(reporteEvento.idReporte())) {
            return;
        }

        RecepcionReporte recepcion = new RecepcionReporte();
        recepcion.setIdReporteGranja(reporteEvento.idReporte());
        recepcion.setNumeroReporteGranja(reporteEvento.numeroReporte());
        recepcion.setFechaReporte(reporteEvento.fecha());
        recepcion.setEstado(EstadoRecepcion.PENDIENTE);

        RecepcionReporte guardada = recepcionRepo.save(recepcion);

        List<LoteOrigenReporte> lotesOrigen = reporteEvento.detalles().stream()
                .map(detalle -> construirLoteOrigen(detalle, guardada)).toList();

        List<LoteOrigenReporte> lotesGuardados = loteOrigenRepo.saveAll(lotesOrigen);

        lotesGuardados.forEach(this::autogenerarFusionDeUnLote);
        aplicarPlantillasRecurrentes(guardada, lotesGuardados);
    }

    @Override
    public RecepcionReporteDTO obtenerPorId(Long id) {
        return construirDTOCompleto(buscarRecepcion(id));
    }

    @Override
    public List<RecepcionReporteDTO> listarTodos() {
        return recepcionRepo.findAll().stream().map(this::construirDTOCompleto).toList();
    }

    @Override
    @Transactional
    public RecepcionReporteDTO confirmarRecepcion(Long id, ConfirmarRecepcionRequestDTO request) {
        RecepcionReporte recepcion = buscarRecepcion(id);

        if (recepcion.getEstado() != EstadoRecepcion.PENDIENTE) {
            throw new IllegalStateException("Esta recepcion ya fue confirmada anteriormente");
        }

        recepcion.setEstado(EstadoRecepcion.RECIBIDO);

        RecepcionReporte guardada = recepcionRepo.save(recepcion);

        List<FusionLote> fusiones = fusionLoteRepo.findByRecepcionAndActivaTrue(guardada);
        fusiones.forEach(f -> consumoHuevoService.registrarIngreso(
                f, OrigenConsumo.COMERCIAL_GRANJA, f.getHuevosComercialGuia(),
                guardada.getFechaReporte(), "Ingreso comercial de granja - " + f.getCodigoFusion()));

        LocalTime horaLlegada = (request != null && request.horaLlegada() != null)
                ? request.horaLlegada()
                : LocalTime.now();

        eventPublisher.publishEvent(new ReporteRecibidoConfirmadoEvent(
                new ReporteRecibidoConfirmadoDTO(guardada.getIdReporteGranja(), horaLlegada)));

        return construirDTOCompleto(guardada);
    }
    @Override
    @Transactional
    public ConteoComercialResponseDTO compararConteoComercial(Long idRecepcion, ConteoComercialRequestDTO request) {
        RecepcionReporte recepcion = buscarRecepcion(idRecepcion);

        List<LoteOrigenReporte> lotesOrigen = loteOrigenRepo.findByRecepcion(recepcion);

        Map<Long, Integer> guiaPorLinea = lotesOrigen.stream()
                .collect(Collectors.groupingBy(
                        LoteOrigenReporte::getIdLineaGenetica,
                        Collectors.summingInt(LoteOrigenReporte::getHuevosComercialGuia)));

        Map<Long, String> nombrePorLinea = lotesOrigen.stream()
                .collect(Collectors.toMap(
                        LoteOrigenReporte::getIdLineaGenetica,
                        LoteOrigenReporte::getLineaGeneticaNombre,
                        (a, b) -> a));

        Map<Long, Integer> contadoPorLinea = request.lineas().stream()
                .collect(Collectors.toMap(
                        ConteoLineaGeneticaRequestDTO::idLineaGenetica,
                        ConteoLineaGeneticaRequestDTO::cantidadContada));

        List<ComparacionLineaGeneticaDTO> comparacion = guiaPorLinea.keySet().stream()
                .map(idLinea -> {
                    int guia = guiaPorLinea.getOrDefault(idLinea, 0);
                    int contado = contadoPorLinea.getOrDefault(idLinea, 0);
                    int diferencia = contado - guia;
                    boolean conforme = diferencia == 0;                    return new ComparacionLineaGeneticaDTO(
                            idLinea, nombrePorLinea.get(idLinea), guia, contado, diferencia, conforme);
                })
                .toList();

        Map<Long, List<FusionLote>> fusionesPorLinea = fusionLoteRepo.findByRecepcion(recepcion).stream()
                .collect(Collectors.groupingBy(FusionLote::getIdLineaGenetica));

        List<String> avisosAjuste = new java.util.ArrayList<>();

        conteoComercialRepo.deleteByRecepcion(recepcion);
        for (ComparacionLineaGeneticaDTO c : comparacion) {
            ConteoComercialLinea registro = new ConteoComercialLinea();
            registro.setRecepcion(recepcion);
            registro.setIdLineaGenetica(c.idLineaGenetica());
            registro.setLineaGeneticaNombre(c.nombreLinea());
            registro.setCantidadGuia(c.cantidadGuia());
            registro.setCantidadContada(c.cantidadContada());
            registro.setDiferencia(c.diferencia());
            registro.setConforme(c.conforme());
            conteoComercialRepo.save(registro);

            if (c.diferencia() != 0) {
                List<FusionLote> fusionesLinea = fusionesPorLinea.getOrDefault(c.idLineaGenetica(), List.of());
                List<ConsumoHuevo> filasComercial = fusionesLinea.stream()
                        .flatMap(f -> consumoHuevoRepo.findByFusionLoteAndOrigen(f, OrigenConsumo.COMERCIAL_GRANJA).stream())
                        .toList();

                if (!filasComercial.isEmpty()) {
                    ConsumoHuevo filaBase = filasComercial.get(0);
                    int nuevaCantidad = filaBase.getCantidad() + c.diferencia();

                    if (nuevaCantidad < filaBase.getCantidadDescontada()) {
                        avisosAjuste.add("Linea " + c.nombreLinea() +
                                ": el conteo real es menor a lo ya consumido de ese saldo. Se dejo en el minimo posible (" +
                                filaBase.getCantidadDescontada() + "), revisar manualmente.");
                        nuevaCantidad = filaBase.getCantidadDescontada();
                    }

                    filaBase.setCantidad(nuevaCantidad);
                    filaBase.setObservacion(
                            (filaBase.getObservacion() != null ? filaBase.getObservacion() + " | " : "") +
                                    "Corregido por conteo fisico: guia " + c.cantidadGuia() + " -> contado " + c.cantidadContada());
                    consumoHuevoRepo.save(filaBase);
                }
            }
        }

        int totalGuia = comparacion.stream().mapToInt(ComparacionLineaGeneticaDTO::cantidadGuia).sum();
        int totalContado = comparacion.stream().mapToInt(ComparacionLineaGeneticaDTO::cantidadContada).sum();
        int diferenciaTotal = totalContado - totalGuia;

        return new ConteoComercialResponseDTO(
                idRecepcion, comparacion, totalGuia, totalContado,
                diferenciaTotal, diferenciaTotal == 0,
                avisosAjuste);
    }

    @Override
    @Transactional
    public RecepcionReporteDTO confirmarConteoComercial(Long idRecepcion) {
        RecepcionReporte recepcion = buscarRecepcion(idRecepcion);

        if (recepcion.getEstado() == EstadoRecepcion.PENDIENTE) {
            throw new IllegalStateException("La recepcion aun no fue confirmada como recibida");
        }
        if (recepcion.isConteoComercialConfirmado()) {
            throw new IllegalStateException("El conteo comercial ya fue confirmado, no se puede volver a confirmar");
        }

        List<ConteoComercialLinea> conteo = conteoComercialRepo.findByRecepcion(recepcion);
        if (conteo.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar: falta registrar el conteo comercial de esta recepcion.");
        }

        recepcion.setConteoComercialConfirmado(true);
        if (recepcion.isEmbandejadoConfirmado()) {
            recepcion.setEstado(EstadoRecepcion.PROCESADO);
        }
        recepcionRepo.save(recepcion);

        return construirDTOCompleto(recepcion);
    }
    private LoteOrigenReporte construirLoteOrigen(DetalleLoteEventDTO detalle, RecepcionReporte recepcion) {
        int totalIncubable = 0;
        int totalComercial = 0;
        for (ConteoTipoHuevoEventDTO conteo : detalle.conteos()) {
            if (clasificacionService.esIncubable(conteo.codigoTipoHuevo())) {
                totalIncubable += conteo.cantidad();
            } else {
                totalComercial += conteo.cantidad();
            }
        }
        LoteOrigenReporte loteOrigen = new LoteOrigenReporte();
        loteOrigen.setRecepcion(recepcion);
        loteOrigen.setIdLoteGranja(detalle.idLote());
        loteOrigen.setCodigoLoteGranja(detalle.codigoLote());
        loteOrigen.setIdLineaGenetica(detalle.idLineaGenetica());
        loteOrigen.setLineaGeneticaNombre(detalle.lineaGeneticaNombre());
        loteOrigen.setHuevosIncubablesGuia(totalIncubable);
        loteOrigen.setHuevosComercialGuia(totalComercial);
        return loteOrigen;
    }

    private void autogenerarFusionDeUnLote(LoteOrigenReporte lote) {
    	
    	if (fusionLoteRepo.existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
                lote.getRecepcion(), lote.getIdLineaGenetica(), lote.getCodigoLoteGranja())) {
            throw new IllegalStateException(
                "Reporte con datos inconsistentes: el lote " + lote.getCodigoLoteGranja() +
                " esta duplicado dentro de la misma linea genetica y recepcion");
        }
    	
        FusionLote fusionLote = new FusionLote();
        fusionLote.setRecepcion(lote.getRecepcion());
        fusionLote.setIdLineaGenetica(lote.getIdLineaGenetica());
        fusionLote.setLineaGeneticaNombre(lote.getLineaGeneticaNombre());
        fusionLote.setCodigoFusion(lote.getCodigoLoteGranja());
        fusionLote.setHuevosIncubablesGuia(lote.getHuevosIncubablesGuia());
        fusionLote.setHuevosComercialGuia(lote.getHuevosComercialGuia());
        fusionLote.setActiva(true);

        FusionLote guardada = fusionLoteRepo.save(fusionLote);

        FusionLoteDetalle detalle = new FusionLoteDetalle();
        detalle.setFusionLote(guardada);
        detalle.setLoteOrigenReporte(lote);
        fusionDetalleRepo.save(detalle);
    }

    private RecepcionReporteDTO construirDTOCompleto(RecepcionReporte recepcion) {
        List<LoteOrigenReporte> lotesOrigen = loteOrigenRepo.findByRecepcion(recepcion);

        Map<Long, List<LoteOrigenReporte>> agrupadoPorGenetica = lotesOrigen.stream()
                .collect(Collectors.groupingBy(LoteOrigenReporte::getIdLineaGenetica));

        List<LineaGeneticaRecepcionDTO> lineasDTO = agrupadoPorGenetica.entrySet().stream().map(entry -> {
                List<LoteOrigenReporte> grupo = entry.getValue();
                String nombre = grupo.get(0).getLineaGeneticaNombre();
                int totalIncubable = grupo.stream().mapToInt(LoteOrigenReporte::getHuevosIncubablesGuia).sum();
                int totalComercial = grupo.stream().mapToInt(LoteOrigenReporte::getHuevosComercialGuia).sum();
                List<LoteOrigenReporteDTO> lotesDTO = grupo.stream()
                    .map(l -> new LoteOrigenReporteDTO(
                        l.getIdLoteGranja(), l.getCodigoLoteGranja(),
                        l.getHuevosIncubablesGuia(), l.getHuevosComercialGuia()))
                    .toList();
                return new LineaGeneticaRecepcionDTO(
                    entry.getKey(), nombre, totalIncubable, totalComercial,
                    totalIncubable + totalComercial, lotesDTO);
            })
            .toList();

        int grandIncubable = lineasDTO.stream().mapToInt(LineaGeneticaRecepcionDTO::totalHuevosIncubables).sum();
        int grandComercial = lineasDTO.stream().mapToInt(LineaGeneticaRecepcionDTO::totalHuevosComerciales).sum();

        return new RecepcionReporteDTO(
            recepcion.getIdRecepcion(), recepcion.getIdReporteGranja(),
            recepcion.getNumeroReporteGranja(), recepcion.getFechaReporte(),
            recepcion.getEstado().name(),

                recepcion.isEmbandejadoConfirmado(),
                recepcion.isConteoComercialConfirmado(),
            grandIncubable, grandComercial, grandIncubable + grandComercial,
            lineasDTO
        );
    }

    private RecepcionReporte buscarRecepcion(Long id) {
        return recepcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + id));
    }

    private void aplicarPlantillasRecurrentes(RecepcionReporte recepcion, List<LoteOrigenReporte> lotesHoy) {
        Set<Long> idsLineaHoy = lotesHoy.stream()
                .map(LoteOrigenReporte::getIdLineaGenetica).collect(java.util.stream.Collectors.toSet());
        if (idsLineaHoy.isEmpty()) return;

        List<FusionLotePlantilla> plantillas = plantillaRepo
                .findByIdLineaGeneticaInAndActivaTrue(new ArrayList<>(idsLineaHoy));
        if (plantillas.isEmpty()) return;

        Map<Long, LoteOrigenReporte> lotesPorIdLoteGranja = lotesHoy.stream()
                .collect(java.util.stream.Collectors.toMap(LoteOrigenReporte::getIdLoteGranja, l -> l, (a, b) -> a));

        for (FusionLotePlantilla plantilla : plantillas) {
            List<FusionLotePlantillaDetalle> detalles = plantillaDetalleRepo.findByPlantilla(plantilla);

            List<LoteOrigenReporte> lotesCoincidentes = detalles.stream()
                    .map(d -> lotesPorIdLoteGranja.get(d.getIdLoteGranja()))
                    .filter(Objects::nonNull)
                    .toList();

            if (lotesCoincidentes.size() < 2) {
                continue; // no llegaron suficientes lotes hoy para fusionar automaticamente
            }

            fusionarLotesDePlantilla(recepcion, plantilla, lotesCoincidentes);
        }
    }

    private void fusionarLotesDePlantilla(RecepcionReporte recepcion, FusionLotePlantilla plantilla, List<LoteOrigenReporte> lotesCoincidentes) {
        List<FusionLote> fusionesOrigen = lotesCoincidentes.stream()
                .map(lote -> fusionLoteRepo
                        .findByRecepcionAndIdLineaGeneticaAndCodigoFusion(recepcion, plantilla.getIdLineaGenetica(), lote.getCodigoLoteGranja())
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        if (fusionesOrigen.size() < 2) return;

        FusionLote nueva = new FusionLote();
        nueva.setRecepcion(recepcion);
        nueva.setIdLineaGenetica(plantilla.getIdLineaGenetica());
        nueva.setLineaGeneticaNombre(plantilla.getLineaGeneticaNombre());
        nueva.setCodigoFusion(plantilla.getCodigoFusion());
        nueva.setActiva(true);

        int totalIncubable = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosIncubablesGuia).sum();
        int totalComercial = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosComercialGuia).sum();
        nueva.setHuevosIncubablesGuia(totalIncubable);
        nueva.setHuevosComercialGuia(totalComercial);

        FusionLote guardada = fusionLoteRepo.save(nueva);

        List<FusionLoteDetalle> detallesOrigen = fusionDetalleRepo.findByFusionLoteIn(fusionesOrigen);
        detallesOrigen.forEach(d -> d.setFusionLote(guardada));
        fusionDetalleRepo.saveAll(detallesOrigen);

        fusionesOrigen.forEach(f -> f.setActiva(false));
        fusionLoteRepo.saveAll(fusionesOrigen);
    }

}