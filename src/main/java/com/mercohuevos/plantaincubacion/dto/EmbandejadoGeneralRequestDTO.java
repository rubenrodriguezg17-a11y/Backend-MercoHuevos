package com.mercohuevos.plantaincubacion.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmbandejadoGeneralRequestDTO(
    @NotNull 
    Long idRecepcion,
    
    @NotEmpty 
    @Valid 
    List<LineaGeneticaEmbandejadoRequestDTO> lineasGeneticas
) {}