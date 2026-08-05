package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.List;
import com.mercohuevos.plantaincubacion.recepcion.dto.EditarFusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLoteRequestDTO;

public interface IFusionLoteService {
    FusionLoteDTO crear(FusionLoteRequestDTO request);
    FusionLoteDTO editar(Long idFusionLote, EditarFusionLoteRequestDTO request);
    void anular(Long idFusionLote);
    List<FusionLoteDTO> listarActivasPorRecepcion(Long idRecepcion);
    List<FusionLoteDTO> listarAnuladasPorRecepcion(Long idRecepcion);
    List<FusionLoteDTO> listarTodasPorRecepcion(Long idRecepcion);
    FusionLoteDTO obtenerPorId(Long idFusionLote);
}