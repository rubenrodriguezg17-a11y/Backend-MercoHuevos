package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoRequestDTO;
import com.mercohuevos.plantaincubacion.mapper.ICategoriaDespachoMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaDespacho;
import com.mercohuevos.plantaincubacion.repository.ICategoriaDespachoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaDespachoImpl implements ICategoriaDespachoService {

    private final ICategoriaDespachoRepository repository;
    private final ICategoriaDespachoMapper mapper;

    @Override
    public CategoriaDespachoDTO crear(CategoriaDespachoRequestDTO request) {
        CategoriaDespacho entity = mapper.toEntity(request);
        entity.setActivo(true);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CategoriaDespachoDTO obtenerPorId(Long id) {
        return mapper.toDTO(buscarActivo(id));
    }

    @Override
    public List<CategoriaDespachoDTO> listarTodos() {
        return repository.findByActivoTrue().stream().map(mapper::toDTO).toList();
    }

    @Override
    public CategoriaDespachoDTO editar(Long id, CategoriaDespachoRequestDTO request) {
        CategoriaDespacho entity = buscarActivo(id);
        entity.setCodigo(request.codigo());
        entity.setDescripcion(request.descripcion());
        entity.setVendiblePorDefecto(request.vendiblePorDefecto());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void desactivar(Long id) {
        CategoriaDespacho entity = buscarActivo(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private CategoriaDespacho buscarActivo(Long id) {
        CategoriaDespacho entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de despacho no encontrada: " + id));

        if (!entity.isActivo()) {
            throw new EntityNotFoundException("Categoria de despacho no encontrada: " + id);
        }
        return entity;
    }
}