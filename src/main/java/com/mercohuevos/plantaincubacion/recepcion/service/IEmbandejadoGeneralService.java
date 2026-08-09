package com.mercohuevos.plantaincubacion.recepcion.service;

import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface IEmbandejadoGeneralService {

    EmbandejadoGeneralResponseDTO 	guardar(EmbandejadoGeneralRequestDTO request);
    EmbandejadoGeneralResponseDTO 	confirmar(Long idEmbandejadoGeneral);
    EmbandejadoGeneralResponseDTO 	obtenerPorId(Long id);
    List<EmbandejadoGeneralResponseDTO> listarTodos();
    EmbandejadoGeneralResponseDTO editar(Long id, EmbandejadoGeneralRequestDTO request);
}
