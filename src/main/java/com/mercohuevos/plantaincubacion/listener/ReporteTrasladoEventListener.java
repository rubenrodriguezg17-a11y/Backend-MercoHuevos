package com.mercohuevos.plantaincubacion.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mercohuevos.common.event.ReporteTrasladoCreadoEvent;
import com.mercohuevos.plantaincubacion.recepcion.service.IRecepcionReporteService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReporteTrasladoEventListener {

    private final IRecepcionReporteService recepcionService;

    @EventListener
    public void onReporteCreado(ReporteTrasladoCreadoEvent evento) {
        recepcionService.procesarReporteRecibido(evento.getReporte());
    }
}