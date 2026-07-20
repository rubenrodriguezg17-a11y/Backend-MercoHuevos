package com.mercohuevos.granja.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.granja.dto.LineaGeneticaDTO;
import com.mercohuevos.granja.dto.LineaGeneticaRequestDTO;
import com.mercohuevos.granja.mapper.ILineaGeneticaMapper;
import com.mercohuevos.granja.model.LineaGenetica;
import com.mercohuevos.granja.repository.ILineaGeneticaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LineaGeneticaImpl implements ILineaGeneticaService {

    private final ILineaGeneticaRepository repository;
    private final ILineaGeneticaMapper mapper;

    @Override
    public LineaGeneticaDTO crear(LineaGeneticaRequestDTO request) {
        LineaGenetica entity = mapper.toEntity(request);
        entity.setActivo(true);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public LineaGeneticaDTO obtenerPorId(Long id) {
        return mapper.toDTO(buscarActivo(id));
    }

    @Override
    public List<LineaGeneticaDTO> listarTodos() {
        return repository.findByActivoTrue().stream().map(mapper::toDTO).toList();
    }

    @Override
    public LineaGeneticaDTO editar(Long id, LineaGeneticaRequestDTO request) {
        LineaGenetica entity = buscarActivo(id);
        entity.setNombreGen(request.nombreGen());
        entity.setPropositoGen(request.propositoGen());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void desactivar(Long id) {
        LineaGenetica entity = buscarActivo(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private LineaGenetica buscarActivo(Long id) {
        LineaGenetica entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Linea genetica no encontrada: " + id));

        if (!entity.isActivo()) {
            throw new EntityNotFoundException("Linea genetica no encontrada: " + id);
        }
        return entity;
    }
}