package com.mercohuevos.plantaincubacion.incubacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleMirajeLoteRequestDTO(

        @NotNull(message = "Debe elegir un lote obligatoriamente")
        Long idCargaLote,

        @NotNull(message = "Debe indicar la maquina donde esta el lote")
        Long idMaquina,

        @NotNull(message = "La cantidad de huevos infertiles es obligatoria")
        @Min(value = 0, message = "No se aceptan valores negativos")
        Integer huevosInfertiles
) {}