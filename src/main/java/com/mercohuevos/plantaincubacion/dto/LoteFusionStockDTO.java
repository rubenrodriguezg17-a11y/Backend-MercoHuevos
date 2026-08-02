package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

public record LoteFusionStockDTO(
    Long idFusionLote,
    String codigoFusion,
    List<CategoriaStockDTO> categorias,
    Integer totalLote
) {}