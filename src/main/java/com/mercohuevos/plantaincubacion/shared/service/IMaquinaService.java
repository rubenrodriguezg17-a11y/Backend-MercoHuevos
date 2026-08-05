package com.mercohuevos.plantaincubacion.shared.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.shared.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.shared.dto.MaquinaRequestDTO;

public interface IMaquinaService {
	MaquinaDTO 			crear(MaquinaRequestDTO request);
	MaquinaDTO 			obtenerPorId(Long id);
	List<MaquinaDTO> 	listarTodos();
	MaquinaDTO			editar(Long id, MaquinaRequestDTO request);
	MaquinaDTO			cambiarEstado(Long id, String nuevoEstado);	
}
