package com.mercohuevos.plantaincubacion.nacimiento.service;

import com.mercohuevos.plantaincubacion.nacimiento.dto.NacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.nacimiento.dto.RegistrarNacimientoRequestDTO;

public interface INacimientoService {
    NacimientoResponseDTO registrar(Long idCarga, RegistrarNacimientoRequestDTO request);
    NacimientoResponseDTO obtenerPorCarga(Long idCarga);
}