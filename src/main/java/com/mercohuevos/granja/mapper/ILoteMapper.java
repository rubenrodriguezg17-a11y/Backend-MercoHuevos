package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.granja.dto.LoteDTO;
import com.mercohuevos.granja.model.Lote;

@Mapper(componentModel = "spring")
public interface ILoteMapper {

    @Mapping(source = "lineaGenetica.nombreGen", target = "lineaGeneticaNombre")
    @Mapping(target = "totalMortalidad", ignore = true)
    @Mapping(target = "poblacionActual", ignore = true)
    LoteDTO toDTO(Lote entity);
}