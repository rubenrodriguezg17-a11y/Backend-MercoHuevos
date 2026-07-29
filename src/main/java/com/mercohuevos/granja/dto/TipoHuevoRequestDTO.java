package com.mercohuevos.granja.dto;

import com.mercohuevos.granja.enums.ClasificacionHuevo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoHuevoRequestDTO(

        @NotBlank(message = "El codigo es obligatorio")
        String codigo,

        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,

        @NotNull(message = "La clasificacion es obligatoria")
        ClasificacionHuevo clasificacion
) {}