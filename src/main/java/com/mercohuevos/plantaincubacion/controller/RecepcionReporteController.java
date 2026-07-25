package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.RecepcionReporteDTO;
import com.mercohuevos.plantaincubacion.service.IRecepcionReporteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/recepciones")
@RequiredArgsConstructor
public class RecepcionReporteController {

    private final IRecepcionReporteService service;

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionReporteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<RecepcionReporteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<RecepcionReporteDTO> confirmarRecepcion(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmarRecepcion(id));
    }
}