package com.mercohuevos.plantaincubacion.nacimiento.dto;

import java.math.BigDecimal;

public record DetalleNacimientoLoteResponseDTO(
        Long idDetalleNacimiento,
        Long idCargaLote,
        String codigoFusion,
        Integer huevosTransferidos,
        Integer noNacidos,
        Integer totalPollitosNacidos,
        BigDecimal porcentajeNacimiento,
        BigDecimal porcentajeAprovechamiento,
        ClasificacionPollitosResponseDTO clasificacion
) {}