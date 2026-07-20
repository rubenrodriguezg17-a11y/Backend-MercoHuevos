package com.mercohuevos.granja.dto;

import java.math.BigDecimal;
import java.util.List;

public record DetalleLoteReporteResponseDTO(
        String codigoLote,
        String lineaGeneticaNombre,
        Integer cantidadMuertasDelDia,
        Integer cantidadAvesActual,
        Integer edadSemanas,
        BigDecimal porcentajeProduccion,
        Integer totalHuevos,
        List<ConteoTipoHuevoResponseDTO> conteos
) {}