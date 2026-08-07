package com.mercohuevos.plantaincubacion.shared.dto;

public record TipoVacunaDTO(
        Long idTipoVacuna,
        String nombreVacuna,
        String dosisEstandar
) {}