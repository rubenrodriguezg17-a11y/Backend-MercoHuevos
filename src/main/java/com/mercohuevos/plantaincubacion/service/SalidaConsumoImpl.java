package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.SalidaConsumoResponseDTO;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.SalidaConsumo;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.ISalidaConsumoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalidaConsumoImpl implements ISalidaConsumoService {

    private final ISalidaConsumoRepository salidaRepo;
    private final IConsumoHuevoRepository consumoRepo;

    @Override
    public SalidaConsumoResponseDTO registrar(SalidaConsumoRequestDTO request) {

        Integer saldoTotal = consumoRepo.sumSaldoTotalDisponible();

        if (saldoTotal < request.cantidad()) {
            throw new IllegalArgumentException(
                    "Saldo de consumo insuficiente. Disponible: " + saldoTotal +
                    ", solicitado: " + request.cantidad());
        }

        List<ConsumoHuevo> registrosConSaldo = consumoRepo.findConSaldoDisponibleOrdenadoPorFecha();

        int cantidadPorDescontar = request.cantidad();

        for (ConsumoHuevo registro : registrosConSaldo) {
            if (cantidadPorDescontar <= 0) break;

            int saldoRegistro = registro.getCantidad() - registro.getCantidadDescontada();
            int aDescontar = Math.min(saldoRegistro, cantidadPorDescontar);

            registro.setCantidadDescontada(registro.getCantidadDescontada() + aDescontar);
            consumoRepo.save(registro);

            cantidadPorDescontar -= aDescontar;
        }

        SalidaConsumo salida = new SalidaConsumo();
        salida.setFecha(request.fecha());
        salida.setCantidad(request.cantidad());
        salida.setDestino(request.destino() != null ? request.destino() : "Molino");
        salida.setObservacion(request.observacion());
        SalidaConsumo guardada = salidaRepo.save(salida);

        int saldoRestante = consumoRepo.sumSaldoTotalDisponible();

        return new SalidaConsumoResponseDTO(
                guardada.getIdSalida(), guardada.getFecha(), guardada.getCantidad(),
                guardada.getDestino(), saldoRestante
        );
    }
}