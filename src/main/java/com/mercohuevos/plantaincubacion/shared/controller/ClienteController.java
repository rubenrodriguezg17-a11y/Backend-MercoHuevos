package com.mercohuevos.plantaincubacion.shared.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireLecturaPlanta;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
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

    @RequireLecturaPlanta
    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @RequireLecturaPlanta
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @RequireLecturaPlanta
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAllClientes());
    }

    @RequireLecturaPlanta
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> editar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.ok(service.edit(request, id));
    }

    @RequireLecturaPlanta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}