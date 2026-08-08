package com.mercohuevos.plantaincubacion.recepcion.dto;

public record ComparacionLineaGeneticaDTO(
        Long idLineaGenetica,
        String nombreLinea,
        Integer cantidadGuia,
        Integer cantidadContada,
        Integer diferencia,
        boolean conforme
) {}