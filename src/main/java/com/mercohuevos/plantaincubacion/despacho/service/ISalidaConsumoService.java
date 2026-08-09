package com.mercohuevos.plantaincubacion.despacho.service;

import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoResponseDTO;

import java.util.List;

public interface ISalidaConsumoService {
    SalidaConsumoResponseDTO registrar(SalidaConsumoRequestDTO request);
    List<SalidaConsumoResponseDTO> listarTodos();
    SalidaConsumoResponseDTO obtenerPorId(Long id);
    void anular(Long id);
}