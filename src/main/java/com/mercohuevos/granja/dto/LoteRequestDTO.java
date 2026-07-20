package com.mercohuevos.granja.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteRequestDTO(

        @NotBlank(message = "El codigo de lote es obligatorio")
        String codigoLote,

        @NotNull(message = "La linea genetica es obligatoria")
        Long idLineaGenetica,

        @NotNull(message = "La fecha de ingreso es obligatoria")
        LocalDate fechaIngreso,

        @NotNull(message = "La cantidad de aves inicial es obligatoria")
        @Min(value = 1, message = "La cantidad de aves debe ser mayor a 0")
        Integer cantidadAvesInicial,

        String galpon,   // opcional, no todos lo especifican

        @NotBlank(message = "El estado es obligatorio")
        String estado
) {}