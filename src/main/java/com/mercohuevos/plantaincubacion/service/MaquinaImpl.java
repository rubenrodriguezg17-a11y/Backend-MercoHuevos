package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.dto.MaquinaRequestDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoMaquina;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.mapper.IMaquinaMapper;
import com.mercohuevos.plantaincubacion.model.Maquina;
import com.mercohuevos.plantaincubacion.repository.IMaquinaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaquinaImpl implements IMaquinaService {

    private static final int CAPACIDAD_ESTANDAR_INCUBADORA = 124416;

    private final IMaquinaRepository repository;
    private final IMaquinaMapper mapper;

    @Override
    public MaquinaDTO crear(MaquinaRequestDTO request) {
        TipoMaquina tipo = TipoMaquina.valueOf(request.tipo());

        Maquina maquina = new Maquina();
        maquina.setTipo(tipo);
        maquina.setNumero(request.numero());
        maquina.setCapacidadMaxima(resolverCapacidad(tipo, request.capacidadMaxima()));
        maquina.setEstado(EstadoMaquina.APAGADA);

        return mapper.toDTO(repository.save(maquina));
    }

    @Override
    public MaquinaDTO obtenerPorId(Long id) {
        return mapper.toDTO(buscarMaquina(id));
    }

    @Override
    public List<MaquinaDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public MaquinaDTO editar(Long id, MaquinaRequestDTO request) {
        Maquina maquina = buscarMaquina(id);
        TipoMaquina tipo = TipoMaquina.valueOf(request.tipo());

        maquina.setTipo(tipo);
        maquina.setNumero(request.numero());
        maquina.setCapacidadMaxima(resolverCapacidad(tipo, request.capacidadMaxima()));

        return mapper.toDTO(repository.save(maquina));
    }

    @Override
    public MaquinaDTO cambiarEstado(Long id, String nuevoEstado) {
        Maquina maquina = buscarMaquina(id);
        maquina.setEstado(EstadoMaquina.valueOf(nuevoEstado));
        return mapper.toDTO(repository.save(maquina));
    }

    private Integer resolverCapacidad(TipoMaquina tipo, Integer capacidadRequest) {
        if (tipo == TipoMaquina.INCUBADORA) {
            return capacidadRequest != null ? capacidadRequest : CAPACIDAD_ESTANDAR_INCUBADORA;
        }

        if (capacidadRequest == null) {
            throw new IllegalArgumentException(
                    "La capacidad maxima es obligatoria para maquinas de tipo NACEDORA");
        }
        return capacidadRequest;
    }

    private Maquina buscarMaquina(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + id));
    }
}