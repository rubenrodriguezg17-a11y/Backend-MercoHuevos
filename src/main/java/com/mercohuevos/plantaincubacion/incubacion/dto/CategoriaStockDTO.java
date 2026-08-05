package com.mercohuevos.plantaincubacion.incubacion.dto;

public record CategoriaStockDTO(
    Long idStock,
    Long idCategoriaEmbandejado,
    String codigoCategoria,
    Integer stockDiaAnterior,
    Integer embandejadoDia,
    Integer pasadoACarton,
    Integer cargaIncubadora,
    Integer stockActual
) {}