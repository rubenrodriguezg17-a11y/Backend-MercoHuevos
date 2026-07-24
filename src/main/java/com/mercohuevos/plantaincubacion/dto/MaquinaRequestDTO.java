package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaquinaRequestDTO(
		@NotBlank(message = "El tipo de maquina es obligatorio")
		String tipo,
		
        @NotNull(message = "El numero es obligatorio")
		Integer numero,
		
        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
		Integer capacidadMaxima
		) {}
