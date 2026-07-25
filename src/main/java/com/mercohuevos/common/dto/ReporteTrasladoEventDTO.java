package com.mercohuevos.common.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReporteTrasladoEventDTO(
        Long idReporte,
        String numeroReporte,
        LocalDate fecha,
        LocalTime horaSalida,
        String chofer,
        String placa,
        String encargadoGranja,
        String veterinarioResponsable,
        String observaciones,
        List<DetalleLoteEventDTO> detalles
) {}