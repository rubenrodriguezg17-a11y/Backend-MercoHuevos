package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
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
        salida.setSaldoRestante(saldoRestante);
        SalidaConsumo guardada = salidaRepo.save(salida);

        return toDTO(guardada);
    }

    @Override
    public List<SalidaConsumoResponseDTO> listarTodos() {
        return salidaRepo.findAllByOrderByFechaDesc().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public SalidaConsumoResponseDTO obtenerPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    @Transactional
    public void anular(Long id) {
        SalidaConsumo salida = buscar(id);
        if (Boolean.TRUE.equals(salida.getAnulado())) {
            throw new IllegalStateException("Esta salida ya fue anulada anteriormente");
        }
        consumoHuevoService.revertirSaldo(salida.getCantidad());
        salida.setAnulado(true);
        salidaRepo.save(salida);
    }

    private SalidaConsumo buscar(Long id) {
        return salidaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salida de consumo no encontrada: " + id));
    }

    private SalidaConsumoResponseDTO toDTO(SalidaConsumo s) {
        return new SalidaConsumoResponseDTO(
                s.getIdSalida(), s.getFecha(), s.getCantidad(),
                s.getTipoSalida(), s.getDestino(), s.getSaldoRestante(), s.getAnulado());
    }
}