// despacho/controller/DespachoPollitosController.java
package com.mercohuevos.plantaincubacion.despacho.controller;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<DespachoResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarDespachoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }
}