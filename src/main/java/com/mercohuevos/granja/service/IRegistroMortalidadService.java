package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.granja.dto.RegistroMortalidadRequestDTO;
import com.mercohuevos.granja.dto.RegistroMortalidadResponseDTO;

public interface IRegistroMortalidadService {
	RegistroMortalidadResponseDTO 			registrar(RegistroMortalidadRequestDTO request);
	List<RegistroMortalidadResponseDTO>		listarPorLote(Long idLote);
}
