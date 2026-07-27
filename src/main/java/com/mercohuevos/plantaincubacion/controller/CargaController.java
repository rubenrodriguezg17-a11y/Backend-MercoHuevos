package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.dto.CargaResponseDTO;
import com.mercohuevos.plantaincubacion.service.ICargaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas")
@RequiredArgsConstructor
public class CargaController {

    private final ICargaService service;

    @PostMapping
    public ResponseEntity<CargaResponseDTO> crear(@Valid @RequestBody CargaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    
    @GetMapping
    public ResponseEntity<List<CargaResponseDTO>> listarTodos(){
    	return ResponseEntity.ok(service.listarCargas());
    }
}