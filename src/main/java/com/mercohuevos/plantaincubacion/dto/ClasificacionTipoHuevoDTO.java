package com.mercohuevos.plantaincubacion.dto;

public record ClasificacionTipoHuevoDTO(
        Long idClasificacion,
        String codigoTipoHuevo,
        boolean esIncubable
) {}