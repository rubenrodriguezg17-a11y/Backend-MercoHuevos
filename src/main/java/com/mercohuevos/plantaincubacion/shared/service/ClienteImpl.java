package com.mercohuevos.plantaincubacion.shared.service;

import com.mercohuevos.plantaincubacion.shared.dto.ClienteDTO;
import com.mercohuevos.plantaincubacion.shared.dto.ClienteRequestDTO;
import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.repository.IClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteImpl  implements IClienteService {
    private final IClienteRepository clienteRepo;

    @Override
    public ClienteDTO create(ClienteRequestDTO request) {
        Cliente cliente = new Cliente();
        cliente.setRazonSocial(request.razonSocial());
        cliente.setContacto(request.contacto());
        return toDTO(clienteRepo.save(cliente));
    }

    @Override
    public ClienteDTO edit(ClienteRequestDTO request, Long id) {
        Cliente cliente = buscar(id);
        cliente.setRazonSocial(request.razonSocial());
        cliente.setContacto(request.contacto());
        return toDTO(clienteRepo.save(cliente));
    }

    @Override
    public ClienteDTO getById(Long id) {
        return toDTO(buscar(id)
        );
    }

    @Override
    public List<ClienteDTO> getAllClientes() {
        return clienteRepo.findAll().stream().map(this::toDTO).toList();
    }

    private Cliente buscar(Long id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
    }

    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(c.getIdCliente(), c.getRazonSocial(), c.getContacto());
    }}
