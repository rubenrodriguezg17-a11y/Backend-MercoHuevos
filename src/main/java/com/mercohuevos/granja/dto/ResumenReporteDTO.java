package com.mercohuevos.granja.dto;

import java.util.List;

public record ResumenReporteDTO(
        Integer totalLotes,
        Integer totalAves,
        Integer totalHuevos,
        List<ResumenPorLineaGeneticaDTO> porLineaGenetica
) {}