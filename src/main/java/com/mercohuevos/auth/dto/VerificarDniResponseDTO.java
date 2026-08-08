package com.mercohuevos.auth.dto;

public record VerificarDniResponseDTO(
        boolean existe,
        boolean passwordConfigurada,
        String nombreCompleto
) {}