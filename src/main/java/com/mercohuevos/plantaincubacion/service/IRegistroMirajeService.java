package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.MirajeResumenDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroMirajeRequestDTO;

public interface IRegistroMirajeService {
	MirajeResumenDTO registrar(Long idCarga, RegistroMirajeRequestDTO request);
	MirajeResumenDTO obtenerResumenPorCarga(Long idCarga);
}
