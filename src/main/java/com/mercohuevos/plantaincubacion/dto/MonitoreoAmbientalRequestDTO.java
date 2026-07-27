package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.NotNull;

public record MonitoreoAmbientalRequestDTO(
		
		@NotNull(message = "La humedad es obligatoria")
		Double humedad,
		
		Double temperatura1,
		Double temperatura2,
		Boolean volteo
) {}
