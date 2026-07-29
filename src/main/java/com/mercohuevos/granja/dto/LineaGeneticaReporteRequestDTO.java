package com.mercohuevos.granja.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineaGeneticaReporteRequestDTO(
    @NotNull(message = "El idLineaGenetica es obligatorio")
    Long idLineaGenetica,
    @NotEmpty(message = "Debe incluir al menos un lote")
    @Valid
    List<LoteReporteRequestDTO> lotes
) {}