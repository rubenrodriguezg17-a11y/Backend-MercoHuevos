package com.mercohuevos.plantaincubacion.vacunacion.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireLogistica;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercohuevos.plantaincubacion.vacunacion.dto.OrdenVacunacionResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.service.IOrdenVacunacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/vacunacion")
@RequiredArgsConstructor
@RequireLogistica
public class OrdenVacunacionGeneralController {

    private final IOrdenVacunacionService service;

    @GetMapping
    public ResponseEntity<List<OrdenVacunacionResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAllOrderVacunacion());
    }
}