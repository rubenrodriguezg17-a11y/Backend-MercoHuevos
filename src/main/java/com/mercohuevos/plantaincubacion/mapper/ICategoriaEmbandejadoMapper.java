package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;

@Mapper(componentModel = "spring")
public interface ICategoriaEmbandejadoMapper {

    @Mapping(source = "codigoCategoria", target = "codigo")
    @Mapping(source = "nombreCategoria", target = "descripcion")
    CategoriaEmbandejadoDTO toDTO(CategoriaEmbandejado entity);

    @Mapping(source = "codigo", target = "codigoCategoria")
    @Mapping(source = "descripcion", target = "nombreCategoria")
    @Mapping(target = "idCategoriaEmbandejado", ignore = true)
    @Mapping(target = "activo", ignore = true)
    CategoriaEmbandejado toEntity(CategoriaEmbandejadoRequestDTO dto);
}