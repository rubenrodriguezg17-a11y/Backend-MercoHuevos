package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import com.mercohuevos.granja.dto.LineaGeneticaDTO;
import com.mercohuevos.granja.dto.LineaGeneticaRequestDTO;
import com.mercohuevos.granja.model.LineaGenetica;

@Mapper(componentModel = "spring")
public interface ILineaGeneticaMapper {
    LineaGeneticaDTO toDTO(LineaGenetica entity);
    LineaGenetica toEntity(LineaGeneticaRequestDTO dto);
}