package com.mercohuevos.plantaincubacion.transferencia.dto;

public record LoteDisponibleTransferenciaDTO(
        Long idCargaLote,
        Long idFusionLote,
        String codigoFusion,
        Integer huevosViablesDisponibles
) {}