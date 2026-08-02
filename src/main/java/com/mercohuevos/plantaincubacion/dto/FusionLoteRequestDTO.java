package com.mercohuevos.plantaincubacion.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FusionLoteRequestDTO(
    @NotNull 
    Long idRecepcion,
    
    @NotNull 
    Long idLineaGenetica,
    
    @NotBlank(message = "Debe indicar un nombre para la fusion")
    String nombreFusion,
    
    @Size(min = 2, message = "Una fusion requiere al menos 2 fusiones/lotes de origen")
    List<Long> idsFusionOrigen
) {}