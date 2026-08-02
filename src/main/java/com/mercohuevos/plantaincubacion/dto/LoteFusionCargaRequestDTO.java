package com.mercohuevos.plantaincubacion.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoteFusionCargaRequestDTO(
    @NotNull Long idFusionLote,
    @NotEmpty @Valid List<CategoriaCargaRequestDTO> categoriasCargadas
) {}