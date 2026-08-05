package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.mapper.IDespachoMapper;
import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.despacho.model.CategoriaDespacho;
import com.mercohuevos.plantaincubacion.despacho.model.Despacho;
import com.mercohuevos.plantaincubacion.incubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.despacho.repository.ICategoriaDespachoRepository;
import com.mercohuevos.plantaincubacion.despacho.repository.IDespachoRepository;

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
    @Transactional
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