package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.SalidaConsumoResponseDTO;

public interface ISalidaConsumoService {
    SalidaConsumoResponseDTO registrar(SalidaConsumoRequestDTO request);
}