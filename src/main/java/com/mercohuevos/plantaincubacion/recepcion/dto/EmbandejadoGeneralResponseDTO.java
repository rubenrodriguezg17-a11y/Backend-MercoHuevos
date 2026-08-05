package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.time.LocalDate;
import java.util.List;

public record EmbandejadoGeneralResponseDTO(
    Long idEmbandejadoGeneral,
    Long idRecepcion,
    LocalDate fechaEmbandejado,
    String estado,
    List<LineaGeneticaEmbandejadoResponseDTO> lineasGeneticas
) {}