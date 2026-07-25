package com.mercohuevos.granja.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReporteTrasladoRequestDTO(

        LocalDate fecha,          // opcional: se autocompleta con hoy

        @NotNull(message = "La hora de salida es obligatoria")
        LocalTime horaSalida,

        @NotBlank(message = "El chofer es obligatorio")
        String chofer,

        @NotBlank(message = "La placa es obligatoria")
        String placa,

        String numeroReporte,     // opcional: se autogenera

        @NotBlank(message = "El encargado de granja es obligatorio")
        String encargadoGranja,

        @NotBlank(message = "El veterinario responsable es obligatorio")
        String veterinarioResponsable,

        String observaciones,     // libre, puede quedar vacío sin problema

        @NotEmpty(message = "El reporte debe incluir al menos un lote")
        @Valid
        List<DetalleLoteReporteRequestDTO> detalles
) {}