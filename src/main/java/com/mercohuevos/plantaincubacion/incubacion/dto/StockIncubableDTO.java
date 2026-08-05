package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDate;

public record StockIncubableDTO(
        Long idStock,
        String fusionLoteNombre,
        String categoriaEmbandejadoCodigo,
        LocalDate fecha,
        Integer stockDiaAnterior,
        Integer embandejadoDia,
        Integer pasadoACarton,
        Integer cargaIncubadora,
        Integer stockActual
) {}