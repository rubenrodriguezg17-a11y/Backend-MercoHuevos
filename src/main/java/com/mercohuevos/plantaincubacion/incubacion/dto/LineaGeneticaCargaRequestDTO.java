package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineaGeneticaCargaRequestDTO(
    @NotNull Long idLineaGenetica,
    @NotEmpty @Valid List<LoteFusionCargaRequestDTO> lotesFusion
) {}