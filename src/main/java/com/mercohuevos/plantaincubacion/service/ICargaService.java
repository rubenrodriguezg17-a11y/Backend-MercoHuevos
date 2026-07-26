package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.CargaResponseDTO;

public interface ICargaService {
    CargaResponseDTO crear(CargaRequestDTO request);
    CargaResponseDTO obtenerPorId(Long id);
}