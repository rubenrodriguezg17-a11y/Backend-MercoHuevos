package com.mercohuevos.granja.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireGranja;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.granja.dto.LoteDTO;
import com.mercohuevos.granja.dto.LoteRequestDTO;
import com.mercohuevos.granja.service.ILoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/granja/lotes")
@RequiredArgsConstructor
public class LoteController {

    private final ILoteService service;

    @RequireGranja
    @PostMapping
    @Transactional
    public ResponseEntity<LoteDTO> crear(@Valid @RequestBody LoteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<LoteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireGranja
    @PutMapping("/{id}")
    public ResponseEntity<LoteDTO> editar(
            @PathVariable Long id, @Valid @RequestBody LoteRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @RequireGranja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        service.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }

    @RequireGranja
    @GetMapping("/{id}/poblacion-actual")
    public ResponseEntity<Integer> poblacionActual(@PathVariable Long id) {
        return ResponseEntity.ok(service.calcularPoblacionActual(id));
    }
}