package com.mercohuevos.plantaincubacion.recepcion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConteoCategoriaEmbandejadoRequestDTO(

        @NotNull(message = "La categoria de embandejado es obligatoria")
        Long idCategoriaEmbandejado,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}