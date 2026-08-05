package com.mercohuevos.plantaincubacion.transferencia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.transferencia.dto.*;
import com.mercohuevos.plantaincubacion.transferencia.service.ITransferenciaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransferenciaController {

    private final ITransferenciaService service;

    @PostMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia")
    public ResponseEntity<TransferenciaResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarTransferenciaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia")
    public ResponseEntity<List<TransferenciaResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }

    @GetMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia/lotes-disponibles")
    public ResponseEntity<List<LoteDisponibleTransferenciaDTO>> listarLotesDisponibles(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarLotesDisponibles(idCarga));
    }

    @GetMapping("/api/plantaincubacion/transferencia/nacedoras-disponibles")
    public ResponseEntity<List<NacedoraDisponibleDTO>> listarNacedorasDisponibles() {
        return ResponseEntity.ok(service.listarNacedorasDisponibles());
    }
}