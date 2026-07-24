package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record FusionLoteRequestDTO(

        @NotBlank(message = "El nombre de la fusion es obligatorio")
        String nombre,

        @NotEmpty(message = "Debe seleccionar al menos un lote de granja")
        @Size(max = 3, message = "No se pueden fusionar mas de 3 lotes")
        List<String> codigosLoteGranja
) {}