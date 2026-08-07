package com.mercohuevos.plantaincubacion.vacunacion.dto;

public record DetalleVacunacionClienteLoteResponseDTO(
        Long idDetalleVacunacion,
        Long idDetalleNacimiento,
        Long idCliente,
        String razonSocialCliente,
        Long idTipoVacuna,
        String nombreVacuna,
        Integer machos1raVacunados,
        Integer machos2daVacunados,
        Integer hembras1raVacunadas,
        Integer hembras2daVacunadas,
        Integer totalVacunados
) {}