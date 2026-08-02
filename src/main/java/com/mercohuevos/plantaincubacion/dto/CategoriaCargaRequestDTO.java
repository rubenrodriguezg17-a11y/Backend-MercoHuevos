package com.mercohuevos.plantaincubacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CategoriaCargaRequestDTO(

        @NotNull(message = "La categoria de embandejado es obligatoria")
        Long idCategoriaEmbandejado,

        @NotNull(message = "La cantidad inicial es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidadCargada,
        
        @NotNull(message = "debes elegir una maquina")
        Long idMaquina
) {}