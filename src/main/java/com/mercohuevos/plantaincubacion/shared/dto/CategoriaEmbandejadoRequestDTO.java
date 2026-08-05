package com.mercohuevos.plantaincubacion.shared.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaEmbandejadoRequestDTO(
		@NotBlank(message = "El codigo es obligatorio")
        String codigo,

        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion
) {}
