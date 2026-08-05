package com.mercohuevos.plantaincubacion.incubacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AsignacionMaquinaRequestDTO(

        @NotNull(message = "La maquina es obligatoria")
        Long idMaquina,

        @NotNull(message = "La cantidad asignada es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad
) {}