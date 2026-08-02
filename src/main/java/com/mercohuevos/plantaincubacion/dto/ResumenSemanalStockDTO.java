package com.mercohuevos.plantaincubacion.dto;

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