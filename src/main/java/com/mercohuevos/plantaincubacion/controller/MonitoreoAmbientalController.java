package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.MonitoreoAmbientalRequestDTO;
import com.mercohuevos.plantaincubacion.dto.MonitoreoAmbientalResponseDTO;
import com.mercohuevos.plantaincubacion.service.IMonitoreoAmbientalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/maquinas/{idMaquina}/monitoreo")
@RequiredArgsConstructor
public class MonitoreoAmbientalController {

    private final IMonitoreoAmbientalService service;

    @PostMapping
    public ResponseEntity<MonitoreoAmbientalResponseDTO> registrar(
            @PathVariable Long idMaquina, @Valid @RequestBody MonitoreoAmbientalRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idMaquina, request));
    }

    @GetMapping
    public ResponseEntity<List<MonitoreoAmbientalResponseDTO>> listarPorMaquina(@PathVariable Long idMaquina) {
        return ResponseEntity.ok(service.listarPorMaquina(idMaquina));
    }
}