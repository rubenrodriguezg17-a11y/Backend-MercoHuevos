package com.mercohuevos.plantaincubacion.dto;

import java.math.BigDecimal;
import java.util.List;

public record MirajeResumenDTO(
		Integer cantidadInicial,
		Integer totalNoFecundado,
		Integer cantidadPostMiraje,
		BigDecimal porcentajeMiraje,
		List<RegistroMirajeItemDTO> registros
) {}
