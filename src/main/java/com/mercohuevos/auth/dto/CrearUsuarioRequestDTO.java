package com.mercohuevos.auth.dto;

import com.mercohuevos.auth.enums.Area;
import com.mercohuevos.auth.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CrearUsuarioRequestDTO(
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "EL DNI debe tener exactamente 8 digitos")
        String dni,

        @NotBlank(message = "El nombre completo es obligatorio")
        String nombreCompleto,

        @NotNull(message = "El rol es obligatorio")
        Rol rol,

        Area area
) {}