package com.mercohuevos.plantaincubacion.vacunacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteRequestDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteResponseDTO;

public interface IEsquemaVacunacionClienteService {
    EsquemaVacunacionClienteResponseDTO 		crear(EsquemaVacunacionClienteRequestDTO request);
    EsquemaVacunacionClienteResponseDTO 		obtenerPorId(Long id);
    List<EsquemaVacunacionClienteResponseDTO> 	listarTodos();
    List<EsquemaVacunacionClienteResponseDTO> 	listarPorCliente(Long idCliente);
    EsquemaVacunacionClienteResponseDTO 		editar(Long id, EsquemaVacunacionClienteRequestDTO request);
}