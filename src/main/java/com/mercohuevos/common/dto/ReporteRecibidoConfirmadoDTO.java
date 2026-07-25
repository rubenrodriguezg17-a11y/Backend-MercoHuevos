package com.mercohuevos.common.dto;

public record ReporteRecibidoConfirmadoDTO(
        Long idReporteGranja,
        java.time.LocalTime horaLlegada
) {}