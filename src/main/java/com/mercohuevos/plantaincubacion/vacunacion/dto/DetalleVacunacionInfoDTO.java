package com.mercohuevos.plantaincubacion.vacunacion.dto;

public record DetalleVacunacionInfoDTO(
        Long idDetalleVacunacion,
        Long idCarga,
        Long idCliente,
        Integer machos1raVacunados,
        Integer machos2daVacunados,
        Integer hembras1raVacunadas,
        Integer hembras2daVacunadas
) {}