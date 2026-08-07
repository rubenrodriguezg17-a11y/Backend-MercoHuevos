package com.mercohuevos.plantaincubacion.vacunacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleVacunacionClienteLoteRequestDTO(

        @NotNull(message = "Debe indicar el lote (detalle de nacimiento) a vacunar")
        Long idDetalleNacimiento,

        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "El tipo de vacuna es obligatorio")
        Long idTipoVacuna,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer machos1raVacunados,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer machos2daVacunados,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer hembras1raVacunadas,

        @NotNull @Min(value = 0, message = "No se aceptan valores negativos")
        Integer hembras2daVacunadas
) {}