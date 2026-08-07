package com.mercohuevos.plantaincubacion.shared.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaDTO;
import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaRequestDTO;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;
import com.mercohuevos.plantaincubacion.shared.repository.ITipoVacunaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoVacunaImpl implements ITipoVacunaService {

    private final ITipoVacunaRepository repository;

    @Override
    public TipoVacunaDTO create(TipoVacunaRequestDTO request) {
        TipoVacuna vacuna = new TipoVacuna();
        vacuna.setNombreVacuna(request.nombreVacuna());
        vacuna.setDosisEstandar(request.dosisEstandar());
        return toDTO(repository.save(vacuna));
    }

    @Override
    public TipoVacunaDTO getById(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public List<TipoVacunaDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public TipoVacunaDTO edit(Long id, TipoVacunaRequestDTO request) {
        TipoVacuna vacuna = buscar(id);
        vacuna.setNombreVacuna(request.nombreVacuna());
        vacuna.setDosisEstandar(request.dosisEstandar());
        return toDTO(repository.save(vacuna));
    }

    private TipoVacuna buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vacuna no encontrado: " + id));
    }

    private TipoVacunaDTO toDTO(TipoVacuna v) {
        return new TipoVacunaDTO(v.getIdTipoVacuna(), v.getNombreVacuna(), v.getDosisEstandar());
    }
}