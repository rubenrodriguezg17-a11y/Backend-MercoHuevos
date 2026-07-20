package com.mercohuevos.granja.dto;

import jakarta.validation.constraints.NotBlank;

public record TipoHuevoRequestDTO(

        @NotBlank(message = "El codigo es obligatorio")
        String codigo,

        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,

        boolean esFertil   // primitivo, no puede ser null, no necesita validacion
) {}