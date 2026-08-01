package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

public record LoteFusionadoEmbandejadoResponseDTO(
    Long idFusionLote,
    String fusionLoteNombre,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia,
    Integer rotosTransporte,
    Integer rotosEmbandejado,
    Integer seleccionDescartada,
    Integer totalEmbandejado,
    String observaciones,
    List<ConteoCategoriaEmbandejadoResponseDTO> conteos,
    Integer diferenciaGuia
) {}