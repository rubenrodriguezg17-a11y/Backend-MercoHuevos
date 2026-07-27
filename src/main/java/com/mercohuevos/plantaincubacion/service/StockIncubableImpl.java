package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.mapper.IStockIncubableMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.IStockIncubableRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockIncubableImpl implements IStockIncubableService {

    private final IStockIncubableRepository repository;
    private final IFusionLoteRepository fusionLoteRepo;
    private final IStockIncubableMapper mapper;
    private final IConsumoHuevoRepository consumoRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;

    @Override
    public List<StockIncubableDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<StockIncubableDTO> listarPorFusionLote(Long idFusionLote) {
        FusionLote fusionLote = fusionLoteRepo.findById(idFusionLote)
                .orElseThrow(() -> new EntityNotFoundException("Fusion de lote no encontrada: " + idFusionLote));

        return repository.findByFusionLote(fusionLote).stream().map(mapper::toDTO).toList();
    }
    
    @Override
    public StockIncubableDTO pasarACarton(PasarACartonRequestDTO request) {

        FusionLote fusionLote = fusionLoteRepo.findById(request.idFusionLote())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fusion de lote no encontrada: " + request.idFusionLote()));

        CategoriaEmbandejado categoria = categoriaRepo.findById(request.idCategoriaEmbandejado())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de embandejado no encontrada: " + request.idCategoriaEmbandejado()));

        StockIncubable stock = repository
                .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(
                        fusionLote, categoria, request.fecha().plusDays(1))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay stock incubable registrado para este lote y categoria"));

        if (stock.getStockActual() < request.cantidad()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para pasar a carton. Disponible: " + stock.getStockActual() +
                    ", solicitado: " + request.cantidad());
        }

        stock.setPasadoACarton(stock.getPasadoACarton() + request.cantidad());
        stock.setStockActual(stock.getStockActual() - request.cantidad());
        StockIncubable actualizado = repository.save(stock);

        ConsumoHuevo consumo = new ConsumoHuevo();
        consumo.setFusionLote(fusionLote);
        consumo.setFecha(request.fecha());
        consumo.setOrigen(OrigenConsumo.PASADO_A_CARTON);
        consumo.setCantidad(request.cantidad());
        consumo.setObservacion(request.observacion() != null ? request.observacion() : "Pasado a carton manual");
        consumoRepo.save(consumo);

        return mapper.toDTO(actualizado);
    }
}