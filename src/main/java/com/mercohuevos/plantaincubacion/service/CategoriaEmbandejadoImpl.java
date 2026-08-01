package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.mapper.ICategoriaEmbandejadoMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaEmbandejadoImpl implements ICategoriaEmbandejadoService {

    private final ICategoriaEmbandejadoRepository repository;
    private final ICategoriaEmbandejadoMapper mapper;

    @Override
    public CategoriaEmbandejadoDTO crear(CategoriaEmbandejadoRequestDTO request) {
        CategoriaEmbandejado entity = mapper.toEntity(request);
        entity.setActivo(true);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CategoriaEmbandejadoDTO obtenerPorId(Long id) {
        return mapper.toDTO(buscarActivo(id));
    }

    @Override
    public List<CategoriaEmbandejadoDTO> listarTodos() {
        return repository.findByActivoTrue().stream().map(mapper::toDTO).toList();
    }

    @Override
    public CategoriaEmbandejadoDTO editar(Long id, CategoriaEmbandejadoRequestDTO request) {
        CategoriaEmbandejado entity = buscarActivo(id);
        entity.setCodigoCategoria(request.codigo());
        entity.setNombreCategoria(request.descripcion());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public void desactivar(Long id) {
        CategoriaEmbandejado entity = buscarActivo(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private CategoriaEmbandejado buscarActivo(Long id) {
        CategoriaEmbandejado entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de embandejado no encontrada: " + id));

        if (!entity.getActivo()) {
            throw new EntityNotFoundException("Categoria de embandejado no encontrada: " + id);
        }
        return entity;
    }
}