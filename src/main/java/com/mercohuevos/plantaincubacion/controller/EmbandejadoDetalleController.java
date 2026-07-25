package com.mercohuevos.plantaincubacion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleRequestDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.service.IEmbandejadoDetalleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/embandejado")
@RequiredArgsConstructor
public class EmbandejadoDetalleController {

    private final IEmbandejadoDetalleService service;

    @PostMapping
    public ResponseEntity<EmbandejadoDetalleResponseDTO> registrar(
            @Valid @RequestBody EmbandejadoDetalleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmbandejadoDetalleResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}