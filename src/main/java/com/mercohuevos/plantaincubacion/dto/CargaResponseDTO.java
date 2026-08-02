package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record CargaResponseDTO(
    LocalDate fechaCarga,
    Integer totalCargadoGlobal,
    List<LineaGeneticaCargaResponseDTO> lineasGeneticas
) {}