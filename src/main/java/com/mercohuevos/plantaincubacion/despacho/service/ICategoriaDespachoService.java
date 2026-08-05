package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.despacho.dto.CategoriaDespachoDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.CategoriaDespachoRequestDTO;

public interface ICategoriaDespachoService {
    CategoriaDespachoDTO 		crear(CategoriaDespachoRequestDTO request);
    CategoriaDespachoDTO	 	obtenerPorId(Long id);
    List<CategoriaDespachoDTO> 	listarTodos();
    CategoriaDespachoDTO 		editar(Long id, CategoriaDespachoRequestDTO request);
    void 						desactivar(Long id);
}