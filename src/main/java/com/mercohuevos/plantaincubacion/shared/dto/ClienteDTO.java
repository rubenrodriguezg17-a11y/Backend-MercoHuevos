package com.mercohuevos.plantaincubacion.shared.dto;

public record ClienteDTO(
        Long idCliente,
        String razonSocial,
        String contacto
) {}