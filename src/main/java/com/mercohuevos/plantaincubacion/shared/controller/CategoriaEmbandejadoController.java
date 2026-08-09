package com.mercohuevos.plantaincubacion.shared.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireEmbandejado;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.shared.dto.CategoriaEmbandejadoDTO;
import com.mercohuevos.plantaincubacion.shared.dto.CategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.shared.service.ICategoriaEmbandejadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/categorias-embandejado")
@RequiredArgsConstructor
public class CategoriaEmbandejadoController {

    private final ICategoriaEmbandejadoService service;

    @RequirePlantaIncubacion
    @PostMapping
    public ResponseEntity<CategoriaEmbandejadoDTO> crear(@Valid @RequestBody CategoriaEmbandejadoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @RequirePlantaIncubacion @RequireEmbandejado
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaEmbandejadoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @RequirePlantaIncubacion @RequireEmbandejado
    @GetMapping
    public ResponseEntity<List<CategoriaEmbandejadoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequirePlantaIncubacion
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEmbandejadoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody CategoriaEmbandejadoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @RequirePlantaIncubacion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}