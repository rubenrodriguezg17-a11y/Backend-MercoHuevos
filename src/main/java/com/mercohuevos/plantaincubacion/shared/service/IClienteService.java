package com.mercohuevos.plantaincubacion.shared.service;

import com.mercohuevos.plantaincubacion.shared.dto.ClienteDTO;
import com.mercohuevos.plantaincubacion.shared.dto.ClienteRequestDTO;

import java.util.List;

public interface IClienteService {
    ClienteDTO          create(ClienteRequestDTO request);
    ClienteDTO          edit(ClienteRequestDTO request, Long id);
    ClienteDTO          getById(Long id);
    List<ClienteDTO>    getAllClientes();
    void                desactivar(Long id);
}
