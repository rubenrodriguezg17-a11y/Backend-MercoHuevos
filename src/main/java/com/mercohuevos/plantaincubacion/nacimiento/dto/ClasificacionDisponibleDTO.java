package com.mercohuevos.plantaincubacion.nacimiento.dto;

public record ClasificacionDisponibleDTO(
        Long idDetalleNacimiento,
        Long idCargaLote,
        String codigoFusion,
        Integer machosPrimera,
        Integer machosSegunda,
        Integer hembrasPrimera,
        Integer hembrasSegunda
) {}