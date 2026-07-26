package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.AsignacionMaquinaResponseDTO;
import com.mercohuevos.plantaincubacion.model.AsignacionCargaMaquina;

@Mapper(componentModel = "spring")
public interface IAsignacionCargaMaquinaMapper {

    @Mapping(source = "maquina.numero", target = "numeroMaquina")
    @Mapping(source = "cantidadAsignada", target = "cantidadAsignada")
    AsignacionMaquinaResponseDTO toResponseDTO(AsignacionCargaMaquina entity);
}