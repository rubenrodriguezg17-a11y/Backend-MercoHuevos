package com.mercohuevos.plantaincubacion.dto;

public record LoteOrigenReporteDTO(
        String codigoLoteGranja,
        String lineaGeneticaNombre,
        Integer huevosIncubablesGuia,
        Integer huevosComercialGuia
) {}