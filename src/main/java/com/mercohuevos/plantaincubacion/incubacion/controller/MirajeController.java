package com.mercohuevos.plantaincubacion.incubacion.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.incubacion.dto.MirajeResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.RegistrarMirajeRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.IMirajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/miraje")
@RequiredArgsConstructor
public class MirajeController {

    private final IMirajeService service;

    @RequireEscrituraEmbandejado
    @PostMapping
    public ResponseEntity<MirajeResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarMirajeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @RequireLecturaEmbandejado
    @GetMapping
    public ResponseEntity<List<MirajeResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }

    @RequireLecturaEmbandejado
    @GetMapping("/{idMiraje}")
    public ResponseEntity<MirajeResponseDTO> obtenerPorId(
            @PathVariable Long idCarga, @PathVariable Long idMiraje) {
        return ResponseEntity.ok(service.obtenerPorId(idCarga, idMiraje));
    }
}