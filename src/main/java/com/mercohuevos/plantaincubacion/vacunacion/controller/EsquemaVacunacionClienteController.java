package com.mercohuevos.plantaincubacion.vacunacion.controller;

import java.util.List;
import com.mercohuevos.auth.annotation.RequireVacunacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteRequestDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.EsquemaVacunacionClienteResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.service.IEsquemaVacunacionClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/esquemas-vacunacion")
@RequiredArgsConstructor
@RequireVacunacion
public class EsquemaVacunacionClienteController {

    private final IEsquemaVacunacionClienteService service;

    @PostMapping
    public ResponseEntity<EsquemaVacunacionClienteResponseDTO> crear(
            @Valid @RequestBody EsquemaVacunacionClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EsquemaVacunacionClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EsquemaVacunacionClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<EsquemaVacunacionClienteResponseDTO>> listarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(service.listarPorCliente(idCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EsquemaVacunacionClienteResponseDTO> editar(
            @PathVariable Long id, @Valid @RequestBody EsquemaVacunacionClienteRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }
}