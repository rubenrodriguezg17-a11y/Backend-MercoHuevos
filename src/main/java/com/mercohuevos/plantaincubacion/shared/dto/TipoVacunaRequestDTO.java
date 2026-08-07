package com.mercohuevos.plantaincubacion.shared.dto;

import jakarta.validation.constraints.NotBlank;

public record TipoVacunaRequestDTO(
        @NotBlank(message = "El nombre de la vacuna es obligatorio")
        String nombreVacuna,

        @NotBlank(message = "La dosis estandar es obligatoria")
        String dosisEstandar
) {}