package com.mercohuevos.plantaincubacion.nacimiento.dto;

public record ClasificacionPollitosResponseDTO(
        Integer machosPrimera,
        Integer machosSegunda,
        Integer hembrasPrimera,
        Integer hembrasSegunda,
        Integer pollitosDescarte,
        Integer totalPollitosViables
) {}