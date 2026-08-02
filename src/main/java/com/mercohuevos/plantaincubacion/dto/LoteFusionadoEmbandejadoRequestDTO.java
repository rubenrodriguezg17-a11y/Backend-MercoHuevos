package com.mercohuevos.plantaincubacion.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoteFusionadoEmbandejadoRequestDTO(
    @NotNull 
    Long idFusionLote,
    
    @Min(0) 
    Integer rotosTransporte,
    
    @Min(0) 
    Integer rotosEmbandejado,
    
    @Min(0) 
    Integer seleccionDescartada,
    
    String observaciones,
    @NotEmpty @Valid List<ConteoCategoriaEmbandejadoRequestDTO> conteos
) {}