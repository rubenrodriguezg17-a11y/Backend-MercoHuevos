package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.mapper.IClasificacionTipoHuevoMapper;
import com.mercohuevos.plantaincubacion.recepcion.model.ClasificacionTipoHuevo;
import com.mercohuevos.plantaincubacion.recepcion.repository.IClasificacionTipoHuevoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClasificacionTipoHuevoImpl implements IClasificacionTipoHuevoService {

    private final IClasificacionTipoHuevoRepository repository;
    private final IClasificacionTipoHuevoMapper mapper;

    @Override
    public ClasificacionTipoHuevoDTO crear(ClasificacionTipoHuevoRequestDTO request) {
        ClasificacionTipoHuevo entity = mapper.toEntity(request);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<ClasificacionTipoHuevoDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public ClasificacionTipoHuevoDTO editar(Long id, ClasificacionTipoHuevoRequestDTO request) {
        ClasificacionTipoHuevo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clasificacion no encontrada: " + id));
        entity.setCodigoTipoHuevo(request.codigoTipoHuevo());
        entity.setEsIncubable(request.esIncubable());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean esIncubable(String codigo) {
        return repository.findByCodigoTipoHuevo(codigo)
                .map(ClasificacionTipoHuevo::isEsIncubable)
                .orElse(false);   // si no esta configurado, por seguridad NO se asume incubable
    }
}