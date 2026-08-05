package com.mercohuevos.plantaincubacion.transferencia.dto;

public record DetalleTransferenciaInfoDTO(
        Long idDetalleTransferencia,
        Long idCarga,
        Long idCargaLote,
        Integer huevosTransferidos,
        Boolean liberado
) {}