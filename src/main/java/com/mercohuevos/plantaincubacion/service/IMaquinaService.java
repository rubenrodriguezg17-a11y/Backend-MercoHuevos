package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.dto.MaquinaRequestDTO;

public interface IMaquinaService {
	MaquinaDTO 			crear(MaquinaRequestDTO request);
	MaquinaDTO 			obtenerPorId(Long id);
	List<MaquinaDTO> 	listarTodos();
	MaquinaDTO			editar(Long id, MaquinaRequestDTO request);
	MaquinaDTO			cambiarEstado(Long id, String nuevoEstado);	
}
