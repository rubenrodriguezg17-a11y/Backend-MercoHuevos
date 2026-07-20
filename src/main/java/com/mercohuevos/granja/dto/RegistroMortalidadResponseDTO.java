package com.mercohuevos.granja.dto;

import java.time.LocalDate;

public record RegistroMortalidadResponseDTO(
		Long idRegistro,
		String codigoLote,
		LocalDate fecha,
		Integer cantidadMuertas,
		String observacion
		) {}
