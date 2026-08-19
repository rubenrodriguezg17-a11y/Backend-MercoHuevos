package com.mercohuevos.auth.dto;

import com.mercohuevos.auth.enums.Area;
import com.mercohuevos.auth.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditarUsuarioRequestDTO(
        @NotBlank(message = "El nombre completo es obligatorio")
        String nombreCompleto,

        @NotNull(message = "El rol es obligatorio")
        Rol rol,

        Area area
) {}