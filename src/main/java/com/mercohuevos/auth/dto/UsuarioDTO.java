package com.mercohuevos.auth.dto;

public record UsuarioDTO(
        Long idUsuario,
        String dni,
        String nombreCompleto,
        String rol,
        String area,
        boolean activo,
        boolean bloqueado
) {}