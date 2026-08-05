package com.mercohuevos.plantaincubacion.despacho.service;

import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoResponseDTO;

public interface ISalidaConsumoService {
    SalidaConsumoResponseDTO registrar(SalidaConsumoRequestDTO request);
}