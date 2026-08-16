package com.mercohuevos.plantaincubacion.recepcion.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.mercohuevos.common.event.TipoHuevoCreadoEvent;
import com.mercohuevos.plantaincubacion.recepcion.model.ClasificacionTipoHuevo;
import com.mercohuevos.plantaincubacion.recepcion.repository.IClasificacionTipoHuevoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TipoHuevoCreadoEventListener {

    private final IClasificacionTipoHuevoRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onTipoHuevoCreado(TipoHuevoCreadoEvent evento) {
        String codigo = evento.getData().codigo();

        if (repository.findByCodigoTipoHuevo(codigo).isPresent()) {
            return;
        }

        ClasificacionTipoHuevo nueva = new ClasificacionTipoHuevo();
        nueva.setCodigoTipoHuevo(codigo);
        nueva.setEsIncubable("FERTIL".equals(evento.getData().clasificacionGranja()));
        nueva.setPendienteRevision(true);
        repository.save(nueva);
    }
}