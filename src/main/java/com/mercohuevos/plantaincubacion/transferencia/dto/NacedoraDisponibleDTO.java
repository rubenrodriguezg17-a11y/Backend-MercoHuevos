package com.mercohuevos.plantaincubacion.transferencia.dto;

public record NacedoraDisponibleDTO(
        Long idMaquina,
        Integer numero,
        Integer capacidadMaxima,
        Integer capacidadDisponible,
        String estado
) {}