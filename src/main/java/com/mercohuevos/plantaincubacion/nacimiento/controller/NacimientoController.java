package com.mercohuevos.plantaincubacion.nacimiento.controller;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.nacimiento.dto.ClasificacionDisponibleDTO;
import com.mercohuevos.plantaincubacion.nacimiento.dto.NacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.nacimiento.dto.RegistrarNacimientoRequestDTO;
import com.mercohuevos.plantaincubacion.nacimiento.service.INacimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/nacimiento")
@RequiredArgsConstructor
public class NacimientoController {

    private final INacimientoService service;

    @RequireEscrituraNacimiento
    @PostMapping
    public ResponseEntity<NacimientoResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarNacimientoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @RequireLecturaVacunacion
    @GetMapping
    public ResponseEntity<NacimientoResponseDTO> obtenerPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.obtenerPorCarga(idCarga));
    }

    @RequireLecturaVacunacion
    @GetMapping("/{idDetalleNacimiento}/clasificacion")
    public ResponseEntity<ClasificacionDisponibleDTO> obtenerClasificacionPorDetalle(
            @PathVariable Long idCarga, @PathVariable Long idDetalleNacimiento) {
        return ResponseEntity.ok(service.obtenerClasificacionPorDetalle(idDetalleNacimiento));
    }
}