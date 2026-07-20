package com.mercohuevos.granja.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.granja.dto.RegistroMortalidadRequestDTO;
import com.mercohuevos.granja.dto.RegistroMortalidadResponseDTO;
import com.mercohuevos.granja.mapper.IRegistroMortalidadMapper;
import com.mercohuevos.granja.model.Lote;
import com.mercohuevos.granja.model.RegistroMortalidad;
import com.mercohuevos.granja.repository.ILoteRepository;
import com.mercohuevos.granja.repository.IRegistroMortalidadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistroMortalidadImpl implements IRegistroMortalidadService{
	
	private final IRegistroMortalidadRepository mortalidadRepo;
	private final ILoteRepository loteRepo;
	private final IRegistroMortalidadMapper mortalidadMapper;

	@Override
	public RegistroMortalidadResponseDTO registrar(RegistroMortalidadRequestDTO request) {
		Lote lote = loteRepo.findById(request.idLote())
				.orElseThrow(() -> new EntityNotFoundException(
						"Lote no encontrado: " + request.idLote()));
		
		RegistroMortalidad registro = new RegistroMortalidad();
		registro.setLote(lote);
		registro.setFecha(request.fecha());
		registro.setCantidadMuertas(request.cantidadMuertas());
		registro.setObservacion(request.observacion());
		
		RegistroMortalidad guardado = mortalidadRepo.save(registro);
		return mortalidadMapper.toResponseDTO(guardado);
	}

	 @Override
	    public List<RegistroMortalidadResponseDTO> listarPorLote(Long idLote) {
	        Lote lote = loteRepo.findById(idLote)
	                .orElseThrow(() -> new EntityNotFoundException("Lote no encontrado: " + idLote));

	        return mortalidadRepo.findByLote(lote).stream()
	                .map(mortalidadMapper::toResponseDTO)
	                .toList();
	    }
}
