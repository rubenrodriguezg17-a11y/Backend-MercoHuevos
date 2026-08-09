package com.mercohuevos.plantaincubacion.despacho.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireLecturaPlantaIncubacion;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.SalidaConsumoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.service.ISalidaConsumoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/salida-consumo")
@RequiredArgsConstructor
public class SalidaConsumoController {

    private final ISalidaConsumoService service;

    @RequirePlantaIncubacion
    @PostMapping
    public ResponseEntity<SalidaConsumoResponseDTO> registrar(@Valid @RequestBody SalidaConsumoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @RequireLecturaPlantaIncubacion @RequireAdmin
    @GetMapping
    public ResponseEntity<List<SalidaConsumoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireLecturaPlantaIncubacion @RequireAdmin
    @GetMapping("/{id}")
    public ResponseEntity<SalidaConsumoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @RequirePlantaIncubacion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        service.anular(id);
        return ResponseEntity.noContent().build();
    }
}