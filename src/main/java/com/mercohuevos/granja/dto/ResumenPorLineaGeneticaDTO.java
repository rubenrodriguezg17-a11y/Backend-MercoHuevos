package com.mercohuevos.granja.dto;

public record ResumenPorLineaGeneticaDTO(
        String lineaGeneticaNombre,
        Integer cantidadLotes,
        Integer totalAves,
        Integer totalHuevos
) {}