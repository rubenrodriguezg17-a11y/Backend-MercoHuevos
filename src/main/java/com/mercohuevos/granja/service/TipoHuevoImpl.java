package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.common.dto.TipoHuevoCreadoEventDTO;
import com.mercohuevos.common.event.TipoHuevoCreadoEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mercohuevos.granja.dto.TipoHuevoDTO;
import com.mercohuevos.granja.dto.TipoHuevoRequestDTO;
import com.mercohuevos.granja.mapper.ITipoHuevoMapper;
import com.mercohuevos.granja.model.TipoHuevo;
import com.mercohuevos.granja.repository.ITipoHuevoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoHuevoImpl implements ITipoHuevoService {

    private final ITipoHuevoRepository repository;
    private final ITipoHuevoMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public TipoHuevoDTO crear(TipoHuevoRequestDTO request) {
        TipoHuevo entity = mapper.toEntity(request);
        entity.setActivo(true);
        TipoHuevo guardado = repository.save(entity);

        eventPublisher.publishEvent(new TipoHuevoCreadoEvent(
                new TipoHuevoCreadoEventDTO(
                        guardado.getCodigo(), guardado.getDescripcion(),
                        guardado.getClasificacion().name())));

        return mapper.toDTO(guardado);
    }

    @Override
    public TipoHuevoDTO obtenerPorId(Long id) {
        return mapper.toDTO(buscarActivo(id));
    }

    @Override
    public List<TipoHuevoDTO> listarTodos() {
        return repository.findByActivoTrue().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public TipoHuevoDTO editar(Long id, TipoHuevoRequestDTO request) {
        TipoHuevo entity = buscarActivo(id);
        entity.setCodigo(request.codigo());
        entity.setDescripcion(request.descripcion());
        entity.setClasificacion(request.clasificacion());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        TipoHuevo entity = buscarActivo(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private TipoHuevo buscarActivo(Long id) {
        TipoHuevo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de huevo no encontrado: " + id));

        if (!entity.isActivo()) {
            throw new EntityNotFoundException("Tipo de huevo no encontrado: " + id);
        }
        return entity;
    }
}