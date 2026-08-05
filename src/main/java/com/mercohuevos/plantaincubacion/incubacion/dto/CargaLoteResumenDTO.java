package com.mercohuevos.plantaincubacion.incubacion.dto;

public record CargaLoteResumenDTO(
        Long idCargaLote,
        Long idFusionLote,
        String codigoFusion,
        Integer cantidadInicial
) {}