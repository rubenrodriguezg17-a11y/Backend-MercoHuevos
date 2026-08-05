package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.util.List;

public record LineaGeneticaRecepcionDTO(
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    Integer totalHuevosIncubables,
    Integer totalHuevosComerciales,
    Integer totalHuevos,
    List<LoteOrigenReporteDTO> lotes
) {}