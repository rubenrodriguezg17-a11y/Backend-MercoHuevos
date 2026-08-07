package com.mercohuevos.plantaincubacion.despacho.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleDespachoLoteRequestDTO(

        @NotNull(message = "Debe indicar el detalle de vacunacion a despachar")
        Long idDetalleVacunacion,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer machos1raDespachados,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer machos2daDespachados,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer hembras1raDespachadas,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer hembras2daDespachadas
) {}