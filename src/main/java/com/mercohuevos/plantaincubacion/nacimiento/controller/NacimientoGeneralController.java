package com.mercohuevos.plantaincubacion.nacimiento.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireLecturaVacunacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercohuevos.plantaincubacion.nacimiento.dto.NacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.nacimiento.service.INacimientoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/nacimiento")
@RequiredArgsConstructor
public class NacimientoGeneralController {

    private final INacimientoService service;

    @RequireLecturaVacunacion
    @GetMapping
    public ResponseEntity<List<NacimientoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAllNacimientos());
    }
}