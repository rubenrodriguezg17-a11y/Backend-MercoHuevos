package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CargaRequestDTO(
        @NotNull(message = "La fecha de carga es obligatoria")
        LocalDate fechaCarga,

        @NotEmpty(message = "Debe incluir al menos una categoria de embandejado")
        @Valid
        List<LineaGeneticaCargaRequestDTO> lineasGeneticas
) {}