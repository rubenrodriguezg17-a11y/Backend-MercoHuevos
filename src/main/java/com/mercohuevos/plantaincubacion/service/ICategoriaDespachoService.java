package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoRequestDTO;

public interface ICategoriaDespachoService {
    CategoriaDespachoDTO 		crear(CategoriaDespachoRequestDTO request);
    CategoriaDespachoDTO	 	obtenerPorId(Long id);
    List<CategoriaDespachoDTO> 	listarTodos();
    CategoriaDespachoDTO 		editar(Long id, CategoriaDespachoRequestDTO request);
    void 						desactivar(Long id);
}