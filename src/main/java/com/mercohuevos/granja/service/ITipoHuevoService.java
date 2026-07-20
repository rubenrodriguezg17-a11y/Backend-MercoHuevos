package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.granja.dto.TipoHuevoDTO;
import com.mercohuevos.granja.dto.TipoHuevoRequestDTO;

public interface ITipoHuevoService {
    TipoHuevoDTO crear(TipoHuevoRequestDTO request);
    TipoHuevoDTO obtenerPorId(Long id);
    List<TipoHuevoDTO> listarTodos();
    TipoHuevoDTO editar(Long id, TipoHuevoRequestDTO request);
    void desactivar(Long id);
}