package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.mercohuevos.granja.dto.ConteoTipoHuevoResponseDTO;
import com.mercohuevos.granja.model.ConteoTipoHuevo;

@Mapper(componentModel = "spring")
public interface IConteoTipoHuevoMapper {

    @Mapping(source = "tipoHuevo.idTipoHuevo", target = "idTipoHuevo")
    @Mapping(source = "tipoHuevo.codigo", target = "codigoTipoHuevo")
    ConteoTipoHuevoResponseDTO toResponseDTO(ConteoTipoHuevo entity);
}
