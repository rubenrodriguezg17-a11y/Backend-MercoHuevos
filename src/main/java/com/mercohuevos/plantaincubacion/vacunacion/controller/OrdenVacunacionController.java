// vacunacion/controller/OrdenVacunacionController.java
package com.mercohuevos.plantaincubacion.vacunacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.vacunacion.dto.OrdenVacunacionResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.RegistrarOrdenVacunacionRequestDTO;
import com.mercohuevos.plantaincubacion.vacunacion.service.IOrdenVacunacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/vacunacion")
@RequiredArgsConstructor
public class OrdenVacunacionController {

    private final IOrdenVacunacionService service;

    @PostMapping
    public ResponseEntity<OrdenVacunacionResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarOrdenVacunacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<List<OrdenVacunacionResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }
}