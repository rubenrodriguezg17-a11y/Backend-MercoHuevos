package com.mercohuevos.auth.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken,
        String nombreCompleto,
        String rol,
        String area
) {}