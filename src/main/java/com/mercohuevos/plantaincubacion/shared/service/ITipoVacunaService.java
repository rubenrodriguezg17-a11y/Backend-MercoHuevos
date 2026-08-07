package com.mercohuevos.plantaincubacion.shared.service;

import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaDTO;
import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaRequestDTO;

import java.util.List;

public interface ITipoVacunaService {
    TipoVacunaDTO           create(TipoVacunaRequestDTO request);
    TipoVacunaDTO           edit(Long id, TipoVacunaRequestDTO request);
    TipoVacunaDTO           getById(Long id);
    List<TipoVacunaDTO>     getAll();
}
