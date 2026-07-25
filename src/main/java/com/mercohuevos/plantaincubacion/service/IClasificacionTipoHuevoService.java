package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.dto.ClasificacionTipoHuevoRequestDTO;

public interface IClasificacionTipoHuevoService {
    ClasificacionTipoHuevoDTO crear(ClasificacionTipoHuevoRequestDTO request);
    List<ClasificacionTipoHuevoDTO> listarTodos();
    ClasificacionTipoHuevoDTO editar(Long id, ClasificacionTipoHuevoRequestDTO request);
    boolean esIncubable(String codigo);   // usado internamente por RecepcionReporte
}