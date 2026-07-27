package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoResponseDTO;

public interface IRegistroNacimientoService {
    RegistroNacimientoResponseDTO registrar(Long idCarga, RegistroNacimientoRequestDTO request);
    RegistroNacimientoResponseDTO obtenerPorCarga(Long idCarga);
}