package com.mercohuevos.plantaincubacion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.MirajeResumenDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroMirajeRequestDTO;
import com.mercohuevos.plantaincubacion.service.IRegistroMirajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/miraje")
@RequiredArgsConstructor
public class RegistroMirajeController {

    private final IRegistroMirajeService service;

    @PostMapping
    public ResponseEntity<MirajeResumenDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistroMirajeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<MirajeResumenDTO> obtenerResumen(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.obtenerResumenPorCarga(idCarga));
    }
}