package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.NotBlank;

public record ClasificacionTipoHuevoRequestDTO(

        @NotBlank(message = "El codigo de tipo de huevo es obligatorio")
        String codigoTipoHuevo,

        boolean esIncubable
) {}