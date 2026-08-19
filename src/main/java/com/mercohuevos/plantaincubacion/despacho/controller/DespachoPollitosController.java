package com.mercohuevos.plantaincubacion.despacho.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.RegistrarDespachoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.service.IDespachoPollitosService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/despacho")
@RequiredArgsConstructor
public class DespachoPollitosController {

    private final IDespachoPollitosService service;

    @RequirePlantaIncubacion
    @PostMapping
    public ResponseEntity<DespachoResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarDespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @RequirePlantaIncubacion
    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }

    @RequirePlantaIncubacion
    @GetMapping("/{idDespacho}")
    public ResponseEntity<DespachoResponseDTO> obtenerPorId(
            @PathVariable Long idCarga, @PathVariable Long idDespacho) {
        return ResponseEntity.ok(service.obtenerPorId(idCarga, idDespacho));
    }
}