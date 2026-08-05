package com.mercohuevos.plantaincubacion.nacimiento.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleNacimientoLoteRequestDTO(
        @NotNull(message = "Debe indicar el detalle de transferencia a liquidar")
        Long idDetalleTransferencia,

        @NotNull(message = "La cantidad de no nacidos es obligatoria")
        @Min(value = 0, message = "No se aceptan valores negativos")
        Integer noNacidos,

        @NotNull(message = "La clasificacion es obligatoria")
        @Valid
        ClasificacionPollitosRequestDTO clasificacion
) {}