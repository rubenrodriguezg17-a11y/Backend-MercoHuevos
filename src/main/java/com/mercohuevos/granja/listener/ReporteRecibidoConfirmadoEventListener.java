package com.mercohuevos.granja.listener;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.stereotype.Component;

import com.mercohuevos.common.event.ReporteRecibidoConfirmadoEvent;
import com.mercohuevos.granja.enums.EstadoReporte;
import com.mercohuevos.granja.repository.IReporteTrasladoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReporteRecibidoConfirmadoEventListener {

    private final IReporteTrasladoRepository reporteRepo;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onRecepcionConfirmada(ReporteRecibidoConfirmadoEvent evento) {
        reporteRepo.findById(evento.getData().idReporteGranja())
                .ifPresent(reporte -> {
                    reporte.setEstado(EstadoReporte.RECIBIDO);
                    reporte.setHoraLlegada(evento.getData().horaLlegada());
                    reporteRepo.save(reporte);
                });
    }
}