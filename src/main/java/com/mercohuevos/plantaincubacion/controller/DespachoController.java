package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.DespachoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.service.IDespachoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final IDespachoService service;

    @PostMapping
    public ResponseEntity<DespachoResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody DespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }
}