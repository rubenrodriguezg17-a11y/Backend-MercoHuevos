package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDate;

public record CargaDisponibleMirajeDTO(
        Long idCarga,
        Integer cantidadInicial,
        Integer totalNoFecundadoAcumulado,
        LocalDate fechaCarga
) {}