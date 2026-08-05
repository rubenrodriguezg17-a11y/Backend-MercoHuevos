package com.mercohuevos.plantaincubacion.recepcion.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mercohuevos.common.dto.ConteoTipoHuevoEventDTO;
import com.mercohuevos.common.dto.DetalleLoteEventDTO;
import com.mercohuevos.common.dto.ReporteRecibidoConfirmadoDTO;
import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.common.event.ReporteRecibidoConfirmadoEvent;
import com.mercohuevos.plantaincubacion.recepcion.dto.LineaGeneticaRecepcionDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.LoteOrigenReporteDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.RecepcionReporteDTO;
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
    private final IClasificacionTipoHuevoService clasificacionService;
    private final ApplicationEventPublisher eventPublisher;

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
    public RecepcionReporteDTO confirmarRecepcion(Long id) {
        RecepcionReporte recepcion = buscarRecepcion(id);

        if (recepcion.getEstado() == EstadoRecepcion.RECIBIDO || recepcion.getEstado() == EstadoRecepcion.PROCESADO) {
            throw new IllegalStateException("Esta recepcion ya fue confirmada anteriormente");
        }

        recepcion.setEstado(EstadoRecepcion.RECIBIDO);

        RecepcionReporte guardada = recepcionRepo.save(recepcion);

        LocalTime horaLlegada = LocalTime.now();

        eventPublisher.publishEvent(new ReporteRecibidoConfirmadoEvent(
                new ReporteRecibidoConfirmadoDTO(guardada.getIdReporteGranja(), horaLlegada)));

        return construirDTOCompleto(guardada);
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
            grandIncubable, grandComercial, grandIncubable + grandComercial,
            lineasDTO
        );
    }

    private RecepcionReporte buscarRecepcion(Long id) {
        return recepcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + id));
    }
}