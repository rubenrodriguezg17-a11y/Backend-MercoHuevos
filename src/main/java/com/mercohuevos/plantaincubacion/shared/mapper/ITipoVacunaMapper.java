package com.mercohuevos.plantaincubacion.shared.mapper;

import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaDTO;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;

public interface ITipoVacunaMapper {
    TipoVacunaDTO toDTO(TipoVacuna entity);
}
