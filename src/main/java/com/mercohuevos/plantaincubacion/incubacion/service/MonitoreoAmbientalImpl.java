package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.incubacion.dto.MonitoreoAmbientalRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.MonitoreoAmbientalResponseDTO;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.incubacion.mapper.IMonitoreoAmbientalMapper;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.incubacion.model.MonitoreoAmbiental;
import com.mercohuevos.plantaincubacion.shared.repository.IMaquinaRepository;
import com.mercohuevos.plantaincubacion.incubacion.repository.IMonitoreoAmbientalRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoreoAmbientalImpl implements IMonitoreoAmbientalService {

    private final IMonitoreoAmbientalRepository repository;
    private final IMaquinaRepository maquinaRepo;
    private final IMonitoreoAmbientalMapper mapper;

    @Override
    public MonitoreoAmbientalResponseDTO registrar(Long idMaquina, MonitoreoAmbientalRequestDTO request) {
        Maquina maquina = maquinaRepo.findById(idMaquina)
                .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + idMaquina));

        validarCamposSegunTipo(maquina, request);

        MonitoreoAmbiental monitoreo = new MonitoreoAmbiental();
        monitoreo.setMaquina(maquina);
        monitoreo.setFechaHora(LocalDateTime.now());
        monitoreo.setHumedad(request.humedad());
        monitoreo.setTemperatura1(request.temperatura1());
        monitoreo.setTemperatura2(request.temperatura2());
        monitoreo.setVolteo(request.volteo());

        return mapper.toResponseDTO(repository.save(monitoreo));
    }

    @Override
    public List<MonitoreoAmbientalResponseDTO> listarPorMaquina(Long idMaquina) {
        Maquina maquina = maquinaRepo.findById(idMaquina)
                .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + idMaquina));

        return repository.findByMaquinaOrderByFechaHoraAsc(maquina).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    private void validarCamposSegunTipo(Maquina maquina, MonitoreoAmbientalRequestDTO request) {
        if (maquina.getTipo() == TipoMaquina.INCUBADORA && request.temperatura1() == null) {
            throw new IllegalArgumentException("La temperatura1 es obligatoria para incubadoras");
        }
        if (maquina.getTipo() == TipoMaquina.NACEDORA && request.temperatura1() == null) {
            throw new IllegalArgumentException("La temperatura es obligatoria para nacedoras");
        }
    }
}