package com.mercohuevos.plantaincubacion.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mercohuevos.common.dto.ConteoTipoHuevoEventDTO;
import com.mercohuevos.common.dto.DetalleLoteEventDTO;
import com.mercohuevos.common.dto.ReporteRecibidoConfirmadoDTO;
import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.common.event.ReporteRecibidoConfirmadoEvent;
import com.mercohuevos.plantaincubacion.dto.LoteOrigenReporteDTO;
import com.mercohuevos.plantaincubacion.dto.RecepcionReporteDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.repository.ILoteOrigenReporteRepository;
import com.mercohuevos.plantaincubacion.repository.IRecepcionReporteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecepcionReporteImpl implements IRecepcionReporteService {

    private final IRecepcionReporteRepository recepcionRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
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
                .map(detalle -> construirLoteOrigen(detalle, guardada))
                .toList();

        loteOrigenRepo.saveAll(lotesOrigen);
    }

    @Override
    public RecepcionReporteDTO obtenerPorId(Long id) {
        return construirDTOCompleto(buscarRecepcion(id));
    }

    @Override
    public List<RecepcionReporteDTO> listarTodos() {
        return recepcionRepo.findAll().stream()
                .map(this::construirDTOCompleto)
                .toList();
    }

    @Override
    @Transactional
    public RecepcionReporteDTO confirmarRecepcion(Long id) {
        RecepcionReporte recepcion = buscarRecepcion(id);

        if (recepcion.getEstado() == EstadoRecepcion.PROCESADO) {
            throw new IllegalStateException("Esta recepcion ya fue confirmada anteriormente");
        }

        recepcion.setEstado(EstadoRecepcion.PROCESADO);
        RecepcionReporte guardada = recepcionRepo.save(recepcion);

        LocalTime horaLlegada = LocalTime.now();

        eventPublisher.publishEvent(new ReporteRecibidoConfirmadoEvent(
                new ReporteRecibidoConfirmadoDTO(guardada.getIdReporteGranja(), horaLlegada)
        ));

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
        loteOrigen.setCodigoLoteGranja(detalle.codigoLote());
        loteOrigen.setLineaGeneticaNombre(detalle.lineaGeneticaNombre());
        loteOrigen.setHuevosIncubablesGuia(totalIncubable);
        loteOrigen.setHuevosComercialGuia(totalComercial);

        return loteOrigen;
    }

    private RecepcionReporteDTO construirDTOCompleto(RecepcionReporte recepcion) {
        List<LoteOrigenReporteDTO> lotes = loteOrigenRepo.findByRecepcion(recepcion).stream()
                .map(l -> new LoteOrigenReporteDTO(
                        l.getCodigoLoteGranja(), l.getLineaGeneticaNombre(),
                        l.getHuevosIncubablesGuia(), l.getHuevosComercialGuia()))
                .toList();

        return new RecepcionReporteDTO(
                recepcion.getIdRecepcion(), recepcion.getIdReporteGranja(),
                recepcion.getNumeroReporteGranja(), recepcion.getFechaReporte(),
                recepcion.getEstado().name(), lotes
        );
    }

    private RecepcionReporte buscarRecepcion(Long id) {
        return recepcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + id));
    }
}