package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistroMirajeRequestDTO(
		
		@NotNull(message = "La fecha es obligatoria")
		LocalDate fecha,
		
		@NotNull(message = "La cantidad no fecundada es obligatoria")
		@Min(value = 0, message = "La cantidad no puede ser menor que 0")
		Integer cantidadNoFecundada
) {}
