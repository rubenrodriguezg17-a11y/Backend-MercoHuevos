package com.mercohuevos.plantaincubacion.dto;


public record FusionLoteDTO(
    Long idFusionLote,
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    String codigoFusion,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia
) {}