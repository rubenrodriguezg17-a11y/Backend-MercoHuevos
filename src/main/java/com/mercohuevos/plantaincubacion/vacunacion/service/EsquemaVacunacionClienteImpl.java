package com.mercohuevos.plantaincubacion.vacunacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;
import com.mercohuevos.plantaincubacion.shared.repository.IClienteRepository;
import com.mercohuevos.plantaincubacion.shared.repository.ITipoVacunaRepository;
import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteRequestDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.model.EsquemaVacunacionCliente;
import com.mercohuevos.plantaincubacion.vacunacion.repository.IEsquemaVacunacionClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EsquemaVacunacionClienteImpl implements IEsquemaVacunacionClienteService {

    private final IEsquemaVacunacionClienteRepository repository;
    private final IClienteRepository clienteRepo;
    private final ITipoVacunaRepository tipoVacunaRepo;

    @Override
    public EsquemaVacunacionClienteResponseDTO crear(EsquemaVacunacionClienteRequestDTO request) {
        Cliente cliente = buscarCliente(request.idCliente());
        TipoVacuna vacuna = buscarVacuna(request.idTipoVacuna());

        EsquemaVacunacionCliente esquema = new EsquemaVacunacionCliente();
        esquema.setCliente(cliente);
        esquema.setTipoVacuna(vacuna);
        esquema.setInstruccionesAplicacion(request.instruccionesAplicacion());

        return toDTO(repository.save(esquema));
    }

    @Override
    public EsquemaVacunacionClienteResponseDTO obtenerPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public List<EsquemaVacunacionClienteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<EsquemaVacunacionClienteResponseDTO> listarPorCliente(Long idCliente) {
        return repository.findByCliente_IdCliente(idCliente).stream().map(this::toDTO).toList();
    }

    @Override
    public EsquemaVacunacionClienteResponseDTO editar(Long id, EsquemaVacunacionClienteRequestDTO request) {
        EsquemaVacunacionCliente esquema = buscar(id);
        esquema.setCliente(buscarCliente(request.idCliente()));
        esquema.setTipoVacuna(buscarVacuna(request.idTipoVacuna()));
        esquema.setInstruccionesAplicacion(request.instruccionesAplicacion());
        return toDTO(repository.save(esquema));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
    }

    private TipoVacuna buscarVacuna(Long id) {
        return tipoVacunaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vacuna no encontrado: " + id));
    }

    private EsquemaVacunacionCliente buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Esquema no encontrado: " + id));
    }

    private EsquemaVacunacionClienteResponseDTO toDTO(EsquemaVacunacionCliente e) {
        return new EsquemaVacunacionClienteResponseDTO(
                e.getIdEsquema(), e.getCliente().getIdCliente(), e.getCliente().getRazonSocial(),
                e.getTipoVacuna().getIdTipoVacuna(), e.getTipoVacuna().getNombreVacuna(),
                e.getInstruccionesAplicacion());
    }
}