package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleRequestDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleResponseDTO;

public interface IEmbandejadoDetalleService {
    EmbandejadoDetalleResponseDTO registrar(EmbandejadoDetalleRequestDTO request);
    EmbandejadoDetalleResponseDTO obtenerPorId(Long id);
}