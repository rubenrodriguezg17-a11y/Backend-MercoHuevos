package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoRequestDTO;

public interface IClasificacionTipoHuevoService {
    ClasificacionTipoHuevoDTO crear(ClasificacionTipoHuevoRequestDTO request);
    List<ClasificacionTipoHuevoDTO> listarTodos();
    ClasificacionTipoHuevoDTO editar(Long id, ClasificacionTipoHuevoRequestDTO request);
    boolean esIncubable(String codigo);   // usado internamente por RecepcionReporte
}