// shared/controller/ClienteController.java
package com.mercohuevos.plantaincubacion.shared.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.shared.dto.ClienteDTO;
import com.mercohuevos.plantaincubacion.shared.dto.ClienteRequestDTO;
import com.mercohuevos.plantaincubacion.shared.service.IClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteService service;

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAllClientes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> editar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.ok(service.edit(request, id));
    }
}