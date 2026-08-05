package com.mercohuevos.plantaincubacion.incubacion.dto;

public record ResumenSemanalStockDTO(
    Integer anio,
    Integer semana,
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    Integer totalEmbandejadoSemana,
    Integer totalCargadoSemana,
    Integer totalPasadoACartonSemana,
    Integer stockFinalSemana
) {}