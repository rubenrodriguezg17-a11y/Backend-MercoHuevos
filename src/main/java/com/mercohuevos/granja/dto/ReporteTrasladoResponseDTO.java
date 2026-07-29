package com.mercohuevos.granja.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReporteTrasladoResponseDTO(
        Long idReporte,
        String numeroReporte,
        LocalDate fecha,
        LocalTime horaSalida,
        LocalTime horaLlegada,
        String chofer,
        String placa,
        String encargadoGranja,
        String veterinarioResponsable,
        String observaciones,
        String estado,
        Integer grandTotalLotes,
        Integer grandTotalAvesActual,
        Integer grandTotalMuertasDelDia,
        Integer grandTotalHuevos,
        List<LineaGeneticaResponseDTO> lineasGeneticas
) {}
