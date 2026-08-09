package com.mercohuevos.plantaincubacion.shared.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireLecturaPlanta;
import com.mercohuevos.auth.annotation.RequireLogistica;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.shared.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.shared.dto.MaquinaRequestDTO;
import com.mercohuevos.plantaincubacion.shared.service.IMaquinaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/maquinas")
@RequiredArgsConstructor
public class MaquinaController {

    private final IMaquinaService service;

    @RequirePlantaIncubacion
    @PostMapping
    public ResponseEntity<MaquinaDTO> crear(@Valid @RequestBody MaquinaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @RequireLecturaPlanta
    @GetMapping("/{id}")
    public ResponseEntity<MaquinaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @RequireLecturaPlanta
    @GetMapping
    public ResponseEntity<List<MaquinaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireLecturaPlanta
    @PutMapping("/{id}")
    public ResponseEntity<MaquinaDTO> editar(
            @PathVariable Long id, @Valid @RequestBody MaquinaRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @RequirePlantaIncubacion
    @PatchMapping("/{id}/estado")
    public ResponseEntity<MaquinaDTO> cambiarEstado(
            @PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }
}