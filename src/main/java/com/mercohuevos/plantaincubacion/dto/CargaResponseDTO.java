package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record CargaResponseDTO(
        Long idCarga,
        String fusionLoteNombre,
        List<CategoriaCargaResponseDTO> categoriasEmbandejado,
        Integer cantidadInicial,
        Integer bandejasCompletas,
        Integer residuoUltimaBandeja,
        LocalDate fechaCarga,
        LocalDate fechaTransferenciaNacedora,
        LocalDate fechaNacimiento,
        String estado,
        List<AsignacionMaquinaResponseDTO> maquinas
) {}