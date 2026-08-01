package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.ConteoCategoriaEmbandejadoResponseDTO;
import com.mercohuevos.plantaincubacion.model.ConteoCategoriaEmbandejado;

@Mapper(componentModel = "spring")
public interface IConteoCategoriaEmbandejadoMapper {

    @Mapping(source = "categoriaEmbandejado.codigoCategoria", target = "codigoCategoria")
    ConteoCategoriaEmbandejadoResponseDTO toResponseDTO(ConteoCategoriaEmbandejado entity);
}