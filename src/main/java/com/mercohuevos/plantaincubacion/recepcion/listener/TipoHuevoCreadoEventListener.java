package com.mercohuevos.plantaincubacion.recepcion.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mercohuevos.common.event.TipoHuevoCreadoEvent;
import com.mercohuevos.plantaincubacion.recepcion.model.ClasificacionTipoHuevo;
import com.mercohuevos.plantaincubacion.recepcion.repository.IClasificacionTipoHuevoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TipoHuevoCreadoEventListener {

    private final IClasificacionTipoHuevoRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTipoHuevoCreado(TipoHuevoCreadoEvent evento) {
        String codigo = evento.getData().codigo();

        if (repository.findByCodigoTipoHuevo(codigo).isPresent()) {
            return;
        }

        // default conservador: solo FERTIL entra como incubable; queda pendiente de que
        // planta confirme, porque granja no conoce las reglas de incubacion de planta.
        ClasificacionTipoHuevo nueva = new ClasificacionTipoHuevo();
        nueva.setCodigoTipoHuevo(codigo);
        nueva.setEsIncubable("FERTIL".equals(evento.getData().clasificacionGranja()));
        nueva.setPendienteRevision(true);
        repository.save(nueva);
    }
}