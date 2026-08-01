package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoResumenDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.mapper.IConsumoHuevoMapper;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumoHuevoImpl implements IConsumoHuevoService {

    private final IConsumoHuevoRepository repository;
    private final IFusionLoteRepository fusionLoteRepo;
    private final IConsumoHuevoMapper mapper;

    @Override
    public List<ConsumoHuevoDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ConsumoHuevoDTO> listarPorFusionLote(Long idFusionLote) {
        FusionLote fusionLote = fusionLoteRepo.findById(idFusionLote)
                .orElseThrow(() -> new EntityNotFoundException("Fusion de lote no encontrada: " + idFusionLote));

        return repository.findByFusionLote(fusionLote).stream().map(mapper::toDTO).toList();
    }

    @Override
    public ConsumoHuevoResumenDTO obtenerResumenPorFusionLote(Long idFusionLote) {
        FusionLote fusionLote = fusionLoteRepo.findById(idFusionLote)
                .orElseThrow(() -> new EntityNotFoundException("Fusion de lote no encontrada: " + idFusionLote));

        List<ConsumoHuevo> registros = repository.findByFusionLote(fusionLote);

        int totalComercial = sumarPorOrigen(registros, OrigenConsumo.COMERCIAL_GRANJA);
        int totalDescarte = sumarPorOrigen(registros, OrigenConsumo.DESCARTE_SELECCION);
        int totalCarton = sumarPorOrigen(registros, OrigenConsumo.PASADO_A_CARTON);
        int totalGeneral = totalComercial + totalDescarte + totalCarton;

        return new ConsumoHuevoResumenDTO(
                fusionLote.getCodigoFusion(), totalComercial, totalDescarte, totalCarton, totalGeneral
        );
    }

    private int sumarPorOrigen(List<ConsumoHuevo> registros, OrigenConsumo origen) {
        return registros.stream()
                .filter(r -> r.getOrigen() == origen)
                .mapToInt(ConsumoHuevo::getCantidad)
                .sum();
    }
}