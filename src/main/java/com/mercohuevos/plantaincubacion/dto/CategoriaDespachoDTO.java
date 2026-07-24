package com.mercohuevos.plantaincubacion.dto;

public record CategoriaDespachoDTO(
        Long idCategoriaDespacho,
        String codigo,
        String descripcion,
        boolean vendiblePorDefecto
) {}