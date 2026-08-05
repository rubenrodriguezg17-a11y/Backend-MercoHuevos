package com.mercohuevos.plantaincubacion.recepcion.dto;

public record ClasificacionTipoHuevoDTO(
        Long idClasificacion,
        String codigoTipoHuevo,
        boolean esIncubable
) {}