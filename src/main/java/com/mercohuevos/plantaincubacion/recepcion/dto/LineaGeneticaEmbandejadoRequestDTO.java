package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineaGeneticaEmbandejadoRequestDTO(
    @NotNull Long idLineaGenetica,
    @NotEmpty @Valid List<LoteFusionadoEmbandejadoRequestDTO> lotesFusionados
) {}