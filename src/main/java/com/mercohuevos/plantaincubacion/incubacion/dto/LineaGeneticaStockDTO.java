package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.util.List;

public record LineaGeneticaStockDTO(
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    Integer totalStockLinea,
    List<LoteFusionStockDTO> lotesFusion
) {}