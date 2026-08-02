package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.CargaDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.CargaResponseDTO;

public interface ICargaService {
    CargaResponseDTO crear(CargaRequestDTO request);
    CargaDetalleResponseDTO obtenerPorId(Long id);
    List<CargaDetalleResponseDTO> listarCargas();
}