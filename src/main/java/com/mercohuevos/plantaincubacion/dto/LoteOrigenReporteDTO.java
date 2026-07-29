package com.mercohuevos.plantaincubacion.dto;

public record LoteOrigenReporteDTO(
    Long idLoteGranja,
    String codigoLoteGranja,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia
) {}