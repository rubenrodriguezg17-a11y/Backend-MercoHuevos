package com.mercohuevos.granja.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConteoTipoHuevoRequestDTO(

        @NotNull(message = "El idTipoHuevo es obligatorio")
        Long idTipoHuevo,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}