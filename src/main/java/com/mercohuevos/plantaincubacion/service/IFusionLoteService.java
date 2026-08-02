package com.mercohuevos.plantaincubacion.service;

import java.util.List;
import com.mercohuevos.plantaincubacion.dto.EditarFusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteRequestDTO;

public interface IFusionLoteService {
    FusionLoteDTO crear(FusionLoteRequestDTO request);
    FusionLoteDTO editar(Long idFusionLote, EditarFusionLoteRequestDTO request);
    void anular(Long idFusionLote);
    List<FusionLoteDTO> listarActivasPorRecepcion(Long idRecepcion);
    List<FusionLoteDTO> listarAnuladasPorRecepcion(Long idRecepcion);
    List<FusionLoteDTO> listarTodasPorRecepcion(Long idRecepcion);
    FusionLoteDTO obtenerPorId(Long idFusionLote);
}