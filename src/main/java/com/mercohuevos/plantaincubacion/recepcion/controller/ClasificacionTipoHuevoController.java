package com.mercohuevos.plantaincubacion.recepcion.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.service.IClasificacionTipoHuevoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/clasificacion-tipo-huevo")
@RequiredArgsConstructor
public class ClasificacionTipoHuevoController {

    private final IClasificacionTipoHuevoService service;

    @RequireEscrituraEmbandejado
    @PostMapping
    public ResponseEntity<ClasificacionTipoHuevoDTO> crear(@Valid @RequestBody ClasificacionTipoHuevoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @RequireLecturaEmbandejado
    @GetMapping
    public ResponseEntity<List<ClasificacionTipoHuevoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireEscrituraEmbandejado
    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionTipoHuevoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody ClasificacionTipoHuevoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @RequireEscrituraEmbandejado
    @GetMapping("/pendientes")
    public ResponseEntity<List<ClasificacionTipoHuevoDTO>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientesRevision());
    }

    @RequireEscrituraEmbandejado
    @PatchMapping("/{id}/validar")
    public ResponseEntity<ClasificacionTipoHuevoDTO> validar(
            @PathVariable Long id, @Valid @RequestBody ClasificacionTipoHuevoRequestDTO request) {
        return ResponseEntity.ok(service.validar(id, request));
    }

    @RequireLecturaEmbandejado
    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionTipoHuevoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @RequireEscrituraEmbandejado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}