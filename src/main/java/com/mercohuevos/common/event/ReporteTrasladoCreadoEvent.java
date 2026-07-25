package com.mercohuevos.common.event;

import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;

public class ReporteTrasladoCreadoEvent {

    private final ReporteTrasladoEventDTO reporte;

    public ReporteTrasladoCreadoEvent(ReporteTrasladoEventDTO reporte) {
        this.reporte = reporte;
    }

    public ReporteTrasladoEventDTO getReporte() {
        return reporte;
    }
}