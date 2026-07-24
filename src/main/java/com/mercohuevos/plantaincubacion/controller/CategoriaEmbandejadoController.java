package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.service.ICategoriaEmbandejadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/categorias-embandejado")
@RequiredArgsConstructor
public class CategoriaEmbandejadoController {

    private final ICategoriaEmbandejadoService service;

    @PostMapping
    public ResponseEntity<CategoriaEmbandejadoDTO> crear(@Valid @RequestBody CategoriaEmbandejadoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaEmbandejadoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaEmbandejadoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEmbandejadoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody CategoriaEmbandejadoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}