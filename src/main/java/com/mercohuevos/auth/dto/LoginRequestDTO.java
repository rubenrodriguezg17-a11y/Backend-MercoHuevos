package com.mercohuevos.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDTO(
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "EL DNI debe tener exactamente 8 digitos")
        String dni,

        @NotBlank(message = "La contrasena es obligatoria")
        String password
) {}