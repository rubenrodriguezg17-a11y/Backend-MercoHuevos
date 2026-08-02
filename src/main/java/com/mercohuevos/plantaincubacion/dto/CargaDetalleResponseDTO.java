package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record CargaDetalleResponseDTO(
    Long idCarga,
    String codigoFusion,
    List<CategoriaCargaResponseDTO> categorias,
    Integer cantidadInicial,
    int bandejasCompletas,
    int residuo,
    LocalDate fechaCarga,
    LocalDate fechaTransferencia,
    LocalDate fechaNacimiento,
    String estado,
    List<AsignacionMaquinaResponseDTO> asignaciones
) {}