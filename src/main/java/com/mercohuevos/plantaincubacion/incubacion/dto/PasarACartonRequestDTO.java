package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PasarACartonRequestDTO(

        @NotNull(message = "La fusion de lote es obligatoria")
        Long idFusionLote,

        @NotNull(message = "La categoria de embandejado es obligatoria")
        Long idCategoriaEmbandejado,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        String observacion
) {}