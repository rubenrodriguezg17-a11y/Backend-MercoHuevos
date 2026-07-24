package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoRequestDTO;
import com.mercohuevos.plantaincubacion.service.ICategoriaDespachoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/categorias-despacho")
@RequiredArgsConstructor
public class CategoriaDespachoController {

    private final ICategoriaDespachoService service;

    @PostMapping
    public ResponseEntity<CategoriaDespachoDTO> crear(@Valid @RequestBody CategoriaDespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDespachoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDespachoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDespachoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody CategoriaDespachoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}