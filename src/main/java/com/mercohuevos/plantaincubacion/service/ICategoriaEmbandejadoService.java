package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoRequestDTO;

public interface ICategoriaEmbandejadoService {
    CategoriaEmbandejadoDTO 		crear(CategoriaEmbandejadoRequestDTO request);
    CategoriaEmbandejadoDTO 		obtenerPorId(Long id);
    List<CategoriaEmbandejadoDTO> 	listarTodos();
    CategoriaEmbandejadoDTO 		editar(Long id, CategoriaEmbandejadoRequestDTO request);
    void 							desactivar(Long id);
}