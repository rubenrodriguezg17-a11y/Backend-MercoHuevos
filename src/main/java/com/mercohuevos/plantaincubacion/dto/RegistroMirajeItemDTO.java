package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;

public record RegistroMirajeItemDTO(
		Long idMraje,
		LocalDate fecha,
		Integer cantidadNofecundada
		) {}
