package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;

@Mapper(componentModel = "spring")
public interface ICategoriaEmbandejadoMapper {
    CategoriaEmbandejadoDTO toDTO(CategoriaEmbandejado entity);
    CategoriaEmbandejado toEntity(CategoriaEmbandejadoRequestDTO dto);
}