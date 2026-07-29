package com.mercohuevos.granja.dto;

import java.util.List;

public record LineaGeneticaResponseDTO(
        Long idLineaGenetica,
        String lineaGeneticaNombre,
        Integer cantidadLotes,
        Integer totalAvesActual,
        Integer totalMuertasDelDia,
        Integer totalHuevos,
        List<DetalleLoteReporteResponseDTO> lotes
) {}
