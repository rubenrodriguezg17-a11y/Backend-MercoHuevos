package com.mercohuevos.granja.dto;

public record TipoHuevoDTO(
        Long idTipoHuevo,
        String codigo,
        String descripcion,
        boolean esFertil
) {}