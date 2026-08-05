package com.mercohuevos.plantaincubacion.despacho.dto;

public record CategoriaDespachoDTO(
        Long idCategoriaDespacho,
        String codigo,
        String descripcion,
        boolean vendiblePorDefecto
) {}