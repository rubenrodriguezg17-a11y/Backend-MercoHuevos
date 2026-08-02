package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record StockIncubableConsultaDTO(
    LocalDate fecha,
    List<LineaGeneticaStockDTO> lineasGeneticas,
    List<ResumenSemanalStockDTO> resumenSemanal
) {}