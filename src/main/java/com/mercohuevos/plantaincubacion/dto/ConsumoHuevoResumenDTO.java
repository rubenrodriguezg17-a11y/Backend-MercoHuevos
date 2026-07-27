package com.mercohuevos.plantaincubacion.dto;

public record ConsumoHuevoResumenDTO(
        String fusionLoteNombre,
        Integer totalComercialGranja,
        Integer totalDescarteSeleccion,
        Integer totalPasadoACarton,
        Integer totalConsumo
) {}