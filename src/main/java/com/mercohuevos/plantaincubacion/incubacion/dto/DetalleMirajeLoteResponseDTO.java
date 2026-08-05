// dto/DetalleMirajeLoteResponseDTO.java
package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.math.BigDecimal;

public record DetalleMirajeLoteResponseDTO(
        Long idDetalleMiraje,
        Long idCargaLote,
        String codigoFusion,
        Long idMaquina,
        Integer numeroMaquina,
        Integer huevosCargados,
        Integer huevosInfertiles,
        Integer huevosViables,
        BigDecimal porcentajeInfertilidad
) {}