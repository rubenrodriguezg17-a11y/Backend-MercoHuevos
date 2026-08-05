package com.mercohuevos.plantaincubacion.recepcion.service;

import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralResponseDTO;

public interface IEmbandejadoGeneralService {

    EmbandejadoGeneralResponseDTO 	guardar(EmbandejadoGeneralRequestDTO request);
    EmbandejadoGeneralResponseDTO 	confirmar(Long idEmbandejadoGeneral);
    EmbandejadoGeneralResponseDTO 	obtenerPorId(Long id);
}
