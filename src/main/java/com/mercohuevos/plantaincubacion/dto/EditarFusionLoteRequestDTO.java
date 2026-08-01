package com.mercohuevos.plantaincubacion.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record EditarFusionLoteRequestDTO(
    @NotBlank(message = "Debe indicar un nombre para la fusion")
    String nombreFusion,
    @NotEmpty(message = "Debe indicar al menos un lote de origen") 
    List<Long> idsLoteOrigen
) {}