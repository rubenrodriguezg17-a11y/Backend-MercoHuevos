package com.mercohuevos.plantaincubacion.incubacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.incubacion.dto.CargaRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaDetalleResponseDTO; // <-- Agregamos esta importación
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaDisponibleMirajeDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.ICargaService;

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
    public ResponseEntity<CargaDetalleResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    
    @GetMapping
    public ResponseEntity<List<CargaDetalleResponseDTO>> listarTodos(){
    	return ResponseEntity.ok(service.listarCargas());
    }
   
    @GetMapping("/miraje-disponibles")
    public ResponseEntity<List<CargaDisponibleMirajeDTO>> listarDisponiblesParaMiraje(
    		@RequestParam Long idLineaGenetica){
    	return ResponseEntity.ok(service.listarDisponiblesParaMirajePorLinea(idLineaGenetica));
    }
}