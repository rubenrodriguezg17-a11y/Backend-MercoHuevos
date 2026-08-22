package com.mercohuevos.plantaincubacion.nacimiento.service;

import com.mercohuevos.plantaincubacion.nacimiento.dto.ClasificacionDisponibleDTO;
import com.mercohuevos.plantaincubacion.nacimiento.dto.NacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.nacimiento.dto.RegistrarNacimientoRequestDTO;

import java.util.List;

public interface INacimientoService {
    NacimientoResponseDTO       registrar(Long idCarga, RegistrarNacimientoRequestDTO request);
    NacimientoResponseDTO       obtenerPorCarga(Long idCarga);
    ClasificacionDisponibleDTO  obtenerClasificacionPorDetalle(Long idDetalleNacimiento);
    List<NacimientoResponseDTO> getAllNacimientos();
}