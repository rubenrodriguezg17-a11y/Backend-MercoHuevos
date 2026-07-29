package com.mercohuevos.granja.dto;

import com.mercohuevos.granja.enums.ClasificacionHuevo;

public record TipoHuevoDTO(
        Long idTipoHuevo,
        String codigo,
        String descripcion,
        ClasificacionHuevo clasificacion
) {}