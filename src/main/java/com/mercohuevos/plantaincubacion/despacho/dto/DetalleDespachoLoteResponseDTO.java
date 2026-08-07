package com.mercohuevos.plantaincubacion.despacho.dto;

public record DetalleDespachoLoteResponseDTO(
        Long idDetalleDespacho,
        Long idDetalleVacunacion,
        Integer machos1raDespachados,
        Integer machos2daDespachados,
        Integer hembras1raDespachadas,
        Integer hembras2daDespachadas,
        Integer totalPollitosDespachados
) {}