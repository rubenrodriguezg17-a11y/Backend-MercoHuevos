package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.util.List;

public record LoteFusionCargaResponseDTO(
    Long idFusionLote,
    String codigoFusion,
    Integer totalCargadoLote,
    List<CategoriaCargaResponseDTO> categoriasCargadas
) {}