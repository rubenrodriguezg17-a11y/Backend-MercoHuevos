package com.mercohuevos.plantaincubacion.recepcion.controller;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ClasificacionTipoHuevoDTO> crear(@Valid @RequestBody ClasificacionTipoHuevoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ClasificacionTipoHuevoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionTipoHuevoDTO> editar(
            @PathVariable Long id, @Valid @RequestBody ClasificacionTipoHuevoRequestDTO request) {
        return ResponseEntity.ok(service.editar(id, request));
    }
}