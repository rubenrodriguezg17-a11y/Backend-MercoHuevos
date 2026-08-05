package com.mercohuevos.plantaincubacion.incubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.incubacion.dto.AsignacionMaquinaResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.model.AsignacionCargaMaquina;

@Mapper(componentModel = "spring")
public interface IAsignacionCargaMaquinaMapper {

    @Mapping(source = "maquina.numero", target = "numeroMaquina")
    @Mapping(source = "cantidadAsignada", target = "cantidadAsignada")
    AsignacionMaquinaResponseDTO toResponseDTO(AsignacionCargaMaquina entity);
}