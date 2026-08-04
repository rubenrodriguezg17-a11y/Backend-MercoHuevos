package com.mercohuevos.plantaincubacion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.MirajeResumenDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroMirajeItemDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroMirajeRequestDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.RegistroMiraje;
import com.mercohuevos.plantaincubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.repository.IRegistroMirajeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistroMirajeImpl implements IRegistroMirajeService {

    private final IRegistroMirajeRepository mirajeRepo;
    private final ICargaRepository cargaRepo;

    @Override
    @Transactional
    public MirajeResumenDTO registrar(Long idCarga, RegistroMirajeRequestDTO request) {
        Carga carga = buscarCarga(idCarga);

        if (carga.getEstado() != EstadoCarga.EN_INCUBACION) {
            throw new IllegalStateException(
                "Solo se puede registrar miraje sobre una carga que este EN_INCUBACION. Estado actual: " + carga.getEstado());
        }

        int yaRegistrado = mirajeRepo.sumNoFecundadoPorCarga(carga);
        int nuevoAcumulado = yaRegistrado + request.cantidadNoFecundada();

        if (nuevoAcumulado > carga.getCantidadInicial()) {
            throw new IllegalArgumentException(
                "El acumulado de no fecundados (" + nuevoAcumulado + ") no puede superar la cantidad cargada (" +
                carga.getCantidadInicial() + "). Ya registrado: " + yaRegistrado);
        }

        RegistroMiraje registro = new RegistroMiraje();
        registro.setCarga(carga);
        registro.setFecha(request.fecha());
        registro.setCantidadNoFecundada(request.cantidadNoFecundada());
        mirajeRepo.save(registro);

        return construirResumen(carga);
    }

    @Override
    public MirajeResumenDTO obtenerResumenPorCarga(Long idCarga) {
        Carga carga = buscarCarga(idCarga);
        return construirResumen(carga);
    }

    private MirajeResumenDTO construirResumen(Carga carga) {
        List<RegistroMiraje> registros = mirajeRepo.findByCarga(carga);

        int totalNoFecundado = mirajeRepo.sumNoFecundadoPorCarga(carga);
        int cantidadPostMiraje = carga.getCantidadInicial() - totalNoFecundado;

        BigDecimal porcentaje = carga.getCantidadInicial() == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(totalNoFecundado)
                    .divide(BigDecimal.valueOf(carga.getCantidadInicial()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

        List<RegistroMirajeItemDTO> registrosDTO = registros.stream()
                .map(r -> new RegistroMirajeItemDTO(r.getIdMiraje(), r.getFecha(), r.getCantidadNoFecundada()))
                .toList();

        return new MirajeResumenDTO(
                carga.getCantidadInicial(), totalNoFecundado, cantidadPostMiraje, porcentaje, registrosDTO
        );
    }

    private Carga buscarCarga(Long id) {
        return cargaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + id));
    }
}