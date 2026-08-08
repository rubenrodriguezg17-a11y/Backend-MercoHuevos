package com.mercohuevos.plantaincubacion.recepcion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConteoLineaGeneticaRequestDTO(

        @NotNull(message = "La linea genetica es obligatoria")
        Long idLineaGenetica,

        @NotNull(message = "La cantidad contada es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer cantidadContada
) {}