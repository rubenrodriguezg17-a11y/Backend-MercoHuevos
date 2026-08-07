package com.mercohuevos.plantaincubacion.despacho.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.model.SalidaConsumo;
import com.mercohuevos.plantaincubacion.despacho.repository.ISalidaConsumoRepository;
import com.mercohuevos.plantaincubacion.incubacion.service.IConsumoHuevoService;

@Service
@RequiredArgsConstructor
public class SalidaConsumoImpl implements ISalidaConsumoService {

    private final ISalidaConsumoRepository salidaRepo;
    private final IConsumoHuevoService consumoHuevoService;

    @Override
    @Transactional
    public SalidaConsumoResponseDTO registrar(SalidaConsumoRequestDTO request) {

        int saldoRestante = consumoHuevoService.descontarSaldo(request.cantidad());

        SalidaConsumo salida = new SalidaConsumo();
        salida.setFecha(request.fecha());
        salida.setCantidad(request.cantidad());
        salida.setTipoSalida(request.tipoSalida());
        salida.setDestino(request.destino());
        salida.setObservacion(request.observacion());
        SalidaConsumo guardada = salidaRepo.save(salida);

        return new SalidaConsumoResponseDTO(
                guardada.getIdSalida(), guardada.getFecha(), guardada.getCantidad(),
                guardada.getTipoSalida(), guardada.getDestino(), saldoRestante
        );
    }
}