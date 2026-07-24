package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.dto.MaquinaRequestDTO;
import com.mercohuevos.plantaincubacion.service.IMaquinaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/maquinas")
@RequiredArgsConstructor
public class MaquinaController {

    private final IMaquinaService service;

    @PostMapping
    public ResponseEntity<MaquinaDTO> crear(@Valid @RequestBody MaquinaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MaquinaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaquinaDTO> editar(
            @PathVariable Long id, @Valid @RequestBody MaquinaRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<MaquinaDTO> cambiarEstado(
            @PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }
}