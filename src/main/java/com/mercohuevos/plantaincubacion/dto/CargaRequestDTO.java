package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CargaRequestDTO(
        @NotNull(message = "El lote es obligatorio")
        Long idFusionLote,

        @NotNull(message = "La fecha de carga es obligatoria")
        LocalDate fechaCarga,

        @NotEmpty(message = "Debe incluir al menos una categoria de embandejado")
        @Valid
        List<CategoriaCargaRequestDTO> categoriasEmbandejado,

        @NotEmpty(message = "Debe asignar al menos una maquina a la carga")
        @Valid
        List<AsignacionMaquinaRequestDTO> maquinas
) {}