package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.DespachoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.mapper.IDespachoMapper;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.CategoriaDespacho;
import com.mercohuevos.plantaincubacion.model.Despacho;
import com.mercohuevos.plantaincubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.repository.ICategoriaDespachoRepository;
import com.mercohuevos.plantaincubacion.repository.IDespachoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DespachoImpl implements IDespachoService {

    private final IDespachoRepository despachoRepo;
    private final ICargaRepository cargaRepo;
    private final ICategoriaDespachoRepository categoriaRepo;
    private final IDespachoMapper mapper;

    @Override
    public DespachoResponseDTO registrar(Long idCarga, DespachoRequestDTO request) {
        Carga carga = cargaRepo.findById(idCarga)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + idCarga));

        CategoriaDespacho categoria = categoriaRepo.findById(request.idCategoriaDespacho())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de despacho no encontrada: " + request.idCategoriaDespacho()));

        Despacho despacho = new Despacho();
        despacho.setCarga(carga);
        despacho.setCategoriaDespacho(categoria);
        despacho.setCliente(request.cliente());
        despacho.setCantidad(request.cantidad());
        despacho.setDestino(request.destino());
        despacho.setFechaDespacho(request.fechaDespacho());

        return mapper.toResponseDTO(despachoRepo.save(despacho));
    }

    @Override
    public List<DespachoResponseDTO> listarPorCarga(Long idCarga) {
        Carga carga = cargaRepo.findById(idCarga)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + idCarga));

        return despachoRepo.findByCarga(carga).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}