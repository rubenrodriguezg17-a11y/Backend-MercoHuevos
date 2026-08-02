package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

public record LineaGeneticaCargaResponseDTO(
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    Integer totalCargadoLinea,
    List<LoteFusionCargaResponseDTO> lotesFusion
) {}