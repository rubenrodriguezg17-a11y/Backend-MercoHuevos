package com.mercohuevos.plantaincubacion.recepcion.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.*;
import com.mercohuevos.plantaincubacion.recepcion.dto.ConfirmarRecepcionRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ConteoComercialRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ConteoComercialResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.recepcion.dto.RecepcionReporteDTO;
import com.mercohuevos.plantaincubacion.recepcion.service.IRecepcionReporteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/recepciones")
@RequiredArgsConstructor
public class RecepcionReporteController {

    private final IRecepcionReporteService service;

    @RequireLecturaEmbandejado
    @GetMapping("/{id}")
    public ResponseEntity<RecepcionReporteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @RequireLecturaEmbandejado
    @GetMapping
    public ResponseEntity<List<RecepcionReporteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireEscrituraEmbandejado
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<RecepcionReporteDTO> confirmarRecepcion(
            @PathVariable Long id,
            @RequestBody(required = false) ConfirmarRecepcionRequestDTO request) {
        return ResponseEntity.ok(service.confirmarRecepcion(id, request));
    }
    @RequireEscrituraEmbandejado
    @PostMapping("/{id}/conteo-comercial")
    public ResponseEntity<ConteoComercialResponseDTO> compararConteoComercial(
            @PathVariable Long id,
            @Valid @RequestBody ConteoComercialRequestDTO request) {
        return ResponseEntity.ok(service.compararConteoComercial(id, request));
    }

    @PatchMapping("/{id}/confirmar-conteo-comercial")
    public RecepcionReporteDTO confirmarConteoComercial(@PathVariable Long id) {
        return service.confirmarConteoComercial(id);
    }
}