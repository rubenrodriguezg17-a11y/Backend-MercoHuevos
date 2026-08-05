package com.mercohuevos.plantaincubacion.recepcion.dto;

import jakarta.validation.constraints.NotBlank;

public record EditarFusionLoteRequestDTO(
    @NotBlank(message = "Debe indicar un nombre para la fusion")
    String nombreFusion
) {}