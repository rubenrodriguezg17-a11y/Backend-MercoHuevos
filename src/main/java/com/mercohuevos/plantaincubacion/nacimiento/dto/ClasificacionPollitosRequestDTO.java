package com.mercohuevos.plantaincubacion.nacimiento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClasificacionPollitosRequestDTO(
        @NotNull @Min(0) Integer machosPrimera,
        @NotNull @Min(0) Integer machosSegunda,
        @NotNull @Min(0) Integer hembrasPrimera,
        @NotNull @Min(0) Integer hembrasSegunda,
        @NotNull @Min(0) Integer pollitosDescarte
) {}