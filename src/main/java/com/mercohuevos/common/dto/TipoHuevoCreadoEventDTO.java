package com.mercohuevos.common.dto;

public record TipoHuevoCreadoEventDTO(
        String codigo,
        String descripcion,
        String clasificacionGranja
) {}