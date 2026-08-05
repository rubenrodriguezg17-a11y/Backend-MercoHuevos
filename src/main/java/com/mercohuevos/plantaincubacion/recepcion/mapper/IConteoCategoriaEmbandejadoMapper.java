package com.mercohuevos.plantaincubacion.recepcion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.recepcion.dto.ConteoCategoriaEmbandejadoResponseDTO;
import com.mercohuevos.plantaincubacion.recepcion.model.ConteoCategoriaEmbandejado;

@Mapper(componentModel = "spring")
public interface IConteoCategoriaEmbandejadoMapper {

    @Mapping(source = "categoriaEmbandejado.codigoCategoria", target = "codigoCategoria")
    ConteoCategoriaEmbandejadoResponseDTO toResponseDTO(ConteoCategoriaEmbandejado entity);
}