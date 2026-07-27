package com.mercohuevos.plantaincubacion.dto;

import java.math.BigDecimal;

public record RegistroNacimientoResponseDTO(
        Long idNacimiento,
        Long idCarga,
        String fusionLoteNombre,
        Integer cantidadMachos,
        Integer cantidadHembras,
        Integer cantidadPrimera,
        Integer cantidadSegunda,
        Integer cantidadDescarte,
        Integer totalNacido,
        BigDecimal porcentajePrimera,
        BigDecimal porcentajeSegunda
) {}