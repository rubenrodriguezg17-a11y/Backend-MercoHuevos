package com.mercohuevos.plantaincubacion.service;

import com.mercohuevos.plantaincubacion.dto.EmbandejadoGeneralRequestDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoGeneralResponseDTO;

public interface IEmbandejadoGeneralService {

    EmbandejadoGeneralResponseDTO 	guardar(EmbandejadoGeneralRequestDTO request);
    EmbandejadoGeneralResponseDTO 	confirmar(Long idEmbandejadoGeneral);
    EmbandejadoGeneralResponseDTO 	obtenerPorId(Long id);
}
