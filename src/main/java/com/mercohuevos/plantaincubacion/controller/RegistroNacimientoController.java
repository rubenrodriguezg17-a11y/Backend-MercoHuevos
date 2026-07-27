package com.mercohuevos.plantaincubacion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.service.IRegistroNacimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/nacimiento")
@RequiredArgsConstructor
public class RegistroNacimientoController {

    private final IRegistroNacimientoService service;

    @PostMapping
    public ResponseEntity<RegistroNacimientoResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistroNacimientoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<RegistroNacimientoResponseDTO> obtenerPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.obtenerPorCarga(idCarga));
    }
}