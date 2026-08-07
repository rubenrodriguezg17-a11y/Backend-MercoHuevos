package com.mercohuevos.plantaincubacion.vacunacion.dto;

public record EsquemaVacunacionClienteResponseDTO(
        Long idEsquema,
        Long idCliente,
        String razonSocialCliente,
        Long idTipoVacuna,
        String nombreVacuna,
        String instruccionesAplicacion
) {}