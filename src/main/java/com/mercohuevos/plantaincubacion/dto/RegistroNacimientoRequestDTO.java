package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistroNacimientoRequestDTO(

        @NotNull(message = "La cantidad de machos es obligatoria")
        @Min(value = 0, message = "No puede ser negativo")
        Integer cantidadMachos,

        @NotNull(message = "La cantidad de hembras es obligatoria")
        @Min(value = 0, message = "No puede ser negativo")
        Integer cantidadHembras,

        @NotNull(message = "La cantidad de primera calidad es obligatoria")
        @Min(value = 0, message = "No puede ser negativo")
        Integer cantidadPrimera,

        @NotNull(message = "La cantidad de segunda calidad es obligatoria")
        @Min(value = 0, message = "No puede ser negativo")
        Integer cantidadSegunda,

        @NotNull(message = "La cantidad de descarte es obligatoria")
        @Min(value = 0, message = "No puede ser negativo")
        Integer cantidadDescarte
) {}