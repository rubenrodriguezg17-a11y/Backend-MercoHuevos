package com.mercohuevos.plantaincubacion.shared.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message = "El nombre o razon social es obligatoria")
        String razonSocial,

        @NotBlank(message = "Debe tener un numero de contacto")
        String contacto
) {}