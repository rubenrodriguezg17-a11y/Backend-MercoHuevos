package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.util.List;

public record LoteFusionStockDTO(
    Long idFusionLote,
    String codigoFusion,
    List<CategoriaStockDTO> categorias,
    Integer totalLote
) {}