package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDespachoRequestDTO(

        @NotBlank(message = "El codigo es obligatorio")
        String codigo,

        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,

        boolean vendiblePorDefecto
) {}