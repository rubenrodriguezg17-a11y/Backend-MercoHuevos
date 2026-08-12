package com.mercohuevos.granja.dto;

import java.time.LocalDate;

public record LoteDTO(
        Long idLote,
        String codigoLote,
        Long idLineaGenetica,
        String lineaGeneticaNombre,
        LocalDate fechaIngreso,
        Integer cantidadAvesInicial,
        String galpon,
        String estado,
        Integer totalMortalidad,      // suma acumulada de mortalidad hasta hoy
        Integer poblacionActual       
) {}