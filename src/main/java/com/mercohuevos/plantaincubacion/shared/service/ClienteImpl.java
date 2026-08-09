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
public class ClienteImpl implements IClienteService {
    private final IClienteRepository clienteRepo;

    @Override
    public ClienteDTO create(ClienteRequestDTO request) {
        Cliente cliente = new Cliente();
        cliente.setRazonSocial(request.razonSocial());
        cliente.setContacto(request.contacto());
        cliente.setActivo(true);
        return toDTO(clienteRepo.save(cliente));
    }

    @Override
    public ClienteDTO edit(ClienteRequestDTO request, Long id) {
        Cliente cliente = buscarActivo(id);
        cliente.setRazonSocial(request.razonSocial());
        cliente.setContacto(request.contacto());
        return toDTO(clienteRepo.save(cliente));
    }

    @Override
    public ClienteDTO getById(Long id) {
        return toDTO(buscarActivo(id));
    }

    @Override
    public List<ClienteDTO> getAllClientes() {
        return clienteRepo.findAll().stream()
                .filter(Cliente::getActivo)
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void desactivar(Long id) {
        Cliente cliente = buscarActivo(id);
        cliente.setActivo(false);
        clienteRepo.save(cliente);
    }

    private Cliente buscarActivo(Long id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
        if (!cliente.getActivo()) {
            throw new EntityNotFoundException("Cliente no encontrado: " + id);
        }
        return cliente;
    }

    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(c.getIdCliente(), c.getRazonSocial(), c.getContacto());
    }
}