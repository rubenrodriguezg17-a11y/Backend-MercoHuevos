package com.mercohuevos.plantaincubacion.transferencia.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireLecturaPlanta;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
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

    @RequirePlantaIncubacion
    @PostMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia")
    public ResponseEntity<TransferenciaResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarTransferenciaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @RequireLecturaPlanta
    @GetMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia")
    public ResponseEntity<List<TransferenciaResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }

    @RequirePlantaIncubacion
    @GetMapping("/api/plantaincubacion/cargas/{idCarga}/transferencia/lotes-disponibles")
    public ResponseEntity<List<LoteDisponibleTransferenciaDTO>> listarLotesDisponibles(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarLotesDisponibles(idCarga));
    }

    @RequirePlantaIncubacion
    @GetMapping("/api/plantaincubacion/transferencia/nacedoras-disponibles")
    public ResponseEntity<List<NacedoraDisponibleDTO>> listarNacedorasDisponibles() {
        return ResponseEntity.ok(service.listarNacedorasDisponibles());
    }
}