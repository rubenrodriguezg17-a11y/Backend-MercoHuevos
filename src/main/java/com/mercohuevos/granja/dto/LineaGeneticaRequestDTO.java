package com.mercohuevos.granja.dto;

import jakarta.validation.constraints.NotBlank;

public record LineaGeneticaRequestDTO(

        @NotBlank(message = "El nombre de la linea genetica es obligatorio")
        String nombreGen,

        @NotBlank(message = "El proposito es obligatorio")
        String propositoGen
) {}