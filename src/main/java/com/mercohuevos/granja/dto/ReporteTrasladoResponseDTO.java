package com.mercohuevos.granja.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReporteTrasladoResponseDTO(
        Long idReporte,
        String numeroReporte,
        LocalDate fecha,
        LocalTime hora,
        String chofer,
        String placa,
        String encargadoGranja,
        String veterinarioResponsable,
        String observaciones,
        String estado,
        List<DetalleLoteReporteResponseDTO> detalles,
        ResumenReporteDTO resumen
) {}