package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record RecepcionReporteDTO(
    Long idRecepcion,
    Long idReporteGranja,
    String numeroReporteGranja,
    LocalDate fechaReporte,
    String estado,
    Integer grandTotalHuevosIncubables,
    Integer grandTotalHuevosComerciales,
    Integer grandTotalHuevos,
    List<LineaGeneticaRecepcionDTO> lineasGeneticas
) {}