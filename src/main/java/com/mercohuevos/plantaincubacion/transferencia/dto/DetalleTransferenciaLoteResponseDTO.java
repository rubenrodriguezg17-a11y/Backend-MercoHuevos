package com.mercohuevos.plantaincubacion.transferencia.dto;

public record DetalleTransferenciaLoteResponseDTO(
        Long idDetalleTransferencia,
        Long idCargaLote,
        String codigoFusion,
        Long idMaquinaOrigen,
        Integer numeroMaquinaOrigen,
        Long idNacedoraDestino,
        Integer numeroNacedoraDestino,
        Integer huevosTransferidos
) {}