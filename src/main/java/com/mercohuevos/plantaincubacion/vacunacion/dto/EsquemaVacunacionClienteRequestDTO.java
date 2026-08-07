package com.mercohuevos.plantaincubacion.vacunacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EsquemaVacunacionClienteRequestDTO(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "El tipo de vacuna es obligatorio")
        Long idTipoVacuna,

        @NotBlank(message = "Las instrucciones de aplicacion son obligatorias")
        String instruccionesAplicacion
) {}