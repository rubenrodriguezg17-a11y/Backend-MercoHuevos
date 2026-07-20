package com.mercohuevos.granja.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistroMortalidadRequestDTO(

        @NotNull(message = "El idLote es obligatorio")
        Long idLote,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La cantidad de muertas es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer cantidadMuertas,

        String observacion   // opcional
) {}