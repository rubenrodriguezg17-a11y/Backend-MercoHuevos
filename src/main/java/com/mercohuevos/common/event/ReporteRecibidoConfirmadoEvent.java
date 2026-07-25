package com.mercohuevos.common.event;

import com.mercohuevos.common.dto.ReporteRecibidoConfirmadoDTO;

public class ReporteRecibidoConfirmadoEvent {

    private final ReporteRecibidoConfirmadoDTO data;

    public ReporteRecibidoConfirmadoEvent(ReporteRecibidoConfirmadoDTO data) {
        this.data = data;
    }

    public ReporteRecibidoConfirmadoDTO getData() {
        return data;
    }
}