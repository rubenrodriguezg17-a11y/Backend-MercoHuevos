package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;

public record ConsumoHuevoDTO(
        Long idConsumo,
        String fusionLoteNombre,
        LocalDate fecha,
        String origen,
        Integer cantidad,
        String observacion
) {}