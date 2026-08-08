package com.mercohuevos.common.event;

import com.mercohuevos.common.dto.TipoHuevoCreadoEventDTO;

public class TipoHuevoCreadoEvent {

    private final TipoHuevoCreadoEventDTO data;

    public TipoHuevoCreadoEvent(TipoHuevoCreadoEventDTO data) {
        this.data = data;
    }

    public TipoHuevoCreadoEventDTO getData() {
        return data;
    }
}
