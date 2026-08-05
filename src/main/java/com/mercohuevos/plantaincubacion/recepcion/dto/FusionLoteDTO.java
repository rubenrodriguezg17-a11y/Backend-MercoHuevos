package com.mercohuevos.plantaincubacion.recepcion.dto;


public record FusionLoteDTO(
    Long idFusionLote,
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    String codigoFusion,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia
) {}