package com.mercohuevos.granja.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.granja.dto.TipoHuevoDTO;
import com.mercohuevos.granja.dto.TipoHuevoRequestDTO;
import com.mercohuevos.granja.service.ITipoHuevoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/granja/tipos-huevo")
@RequiredArgsConstructor
public class TipoHuevoController {

    private final ITipoHuevoService service;

    @PostMapping
    public ResponseEntity<TipoHuevoDTO> crear(@Valid @RequestBody TipoHuevoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHuevoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoHuevoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoHuevoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody TipoHuevoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}