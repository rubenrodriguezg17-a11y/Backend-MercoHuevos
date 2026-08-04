package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;

public record CargaDisponibleMirajeDTO(
    Long idCarga,
    Long idFusionLote,
    String codigoFusion,
    Integer cantidadInicial,
    Integer totalNoFecundadoAcumulado,
    LocalDate fechaCarga
) {}