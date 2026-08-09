package com.mercohuevos.plantaincubacion.shared.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaDTO;
import com.mercohuevos.plantaincubacion.shared.dto.TipoVacunaRequestDTO;
import com.mercohuevos.plantaincubacion.shared.service.ITipoVacunaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/tipos-vacuna")
@RequiredArgsConstructor
public class TipoVacunaController {

    private final ITipoVacunaService service;

    @RequireVacunacion
    @PostMapping
    public ResponseEntity<TipoVacunaDTO> crear(@Valid @RequestBody TipoVacunaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @RequireLecturaVacunacion
    @GetMapping("/{id}")
    public ResponseEntity<TipoVacunaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @RequireLecturaVacunacion
    @GetMapping
    public ResponseEntity<List<TipoVacunaDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAll());
    }

    @RequireVacunacion
    @PutMapping("/{id}")
    public ResponseEntity<TipoVacunaDTO> editar(@PathVariable Long id, @Valid @RequestBody TipoVacunaRequestDTO request) {
        return ResponseEntity.ok(service.edit(id, request));
    }

    @RequireVacunacion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}