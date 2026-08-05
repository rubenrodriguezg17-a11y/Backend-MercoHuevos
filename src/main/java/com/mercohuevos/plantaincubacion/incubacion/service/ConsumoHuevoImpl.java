package com.mercohuevos.plantaincubacion.incubacion.service;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoResumenDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.incubacion.mapper.IConsumoHuevoMapper;
import com.mercohuevos.plantaincubacion.incubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.incubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLoteRepository;

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

    @Override
    @Transactional
    public int descontarSaldo(int cantidad) {
        Integer saldoTotal = repository.sumSaldoTotalDisponible();
        if (saldoTotal < cantidad) {
            throw new IllegalArgumentException("Saldo de consumo insuficiente. Disponible: " + saldoTotal + ", solicitado: " + cantidad);
        }
        List<ConsumoHuevo> registrosConSaldo = repository.findConSaldoDisponibleOrdenadoPorFecha();
        int cantidadPorDescontar = cantidad;
        for (ConsumoHuevo registro : registrosConSaldo) {
            if (cantidadPorDescontar <= 0) break;
            int saldoRegistro = registro.getCantidad() - registro.getCantidadDescontada();
            int aDescontar = Math.min(saldoRegistro, cantidadPorDescontar);
            registro.setCantidadDescontada(registro.getCantidadDescontada() + aDescontar);
            repository.save(registro);
            cantidadPorDescontar -= aDescontar;
        }
        return repository.sumSaldoTotalDisponible();
    }

    private int sumarPorOrigen(List<ConsumoHuevo> registros, OrigenConsumo origen) {
        return registros.stream()
                .filter(r -> r.getOrigen() == origen)
                .mapToInt(ConsumoHuevo::getCantidad)
                .sum();
    }
}