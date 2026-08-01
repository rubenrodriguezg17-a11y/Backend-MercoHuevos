package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

public record FusionLoteDTO(
    Long idFusionLote,
    Long idRecepcion,
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    String codigoFusion,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia,
    List<LoteOrigenSimpleDTO> lotesOrigen
) {}