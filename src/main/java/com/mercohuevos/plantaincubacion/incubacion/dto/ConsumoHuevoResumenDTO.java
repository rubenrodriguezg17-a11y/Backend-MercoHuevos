package com.mercohuevos.plantaincubacion.incubacion.dto;

public record ConsumoHuevoResumenDTO(
        String fusionLoteNombre,
        Integer totalComercialGranja,
        Integer totalDescarteSeleccion,
        Integer totalPasadoACarton,
        Integer totalConsumo
) {}