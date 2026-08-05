package com.mercohuevos.plantaincubacion.shared.dto;

public record MaquinaDTO(
        Long idMaquina,
        String tipo,
        Integer numero,
        Integer capacidadMaxima,
        String estado
) {}