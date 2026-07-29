package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.mercohuevos.granja.dto.ReporteTrasladoResponseDTO;
import com.mercohuevos.granja.model.ReporteTraslado;

@Mapper(componentModel = "spring", uses = { IDetalleLoteReporteMapper.class })
public interface IReporteTrasladoMapper {

    @Mapping(target = "grandTotalLotes", ignore = true)
    @Mapping(target = "grandTotalAvesActual", ignore = true)
    @Mapping(target = "grandTotalMuertasDelDia", ignore = true)
    @Mapping(target = "grandTotalHuevos", ignore = true)
    @Mapping(target = "lineasGeneticas", ignore = true)
    ReporteTrasladoResponseDTO toResponseDTO(ReporteTraslado entity);
}
