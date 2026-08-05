package com.mercohuevos.plantaincubacion.recepcion.dto;

public record LoteOrigenReporteDTO(
    Long idLoteGranja,
    String codigoLoteGranja,
    Integer huevosIncubablesGuia,
    Integer huevosComercialGuia
) {}