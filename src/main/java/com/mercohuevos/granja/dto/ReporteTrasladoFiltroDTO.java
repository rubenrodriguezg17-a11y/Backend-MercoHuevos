package com.mercohuevos.granja.dto;

import java.time.LocalDate;

import com.mercohuevos.granja.enums.EstadoReporte;

public record ReporteTrasladoFiltroDTO(
        EstadoReporte estado,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String chofer) {
}