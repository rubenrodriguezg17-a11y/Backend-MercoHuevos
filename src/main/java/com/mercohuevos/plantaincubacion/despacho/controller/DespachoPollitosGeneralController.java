package com.mercohuevos.plantaincubacion.despacho.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.service.IDespachoPollitosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/despacho")
@RequiredArgsConstructor
public class DespachoPollitosGeneralController {

    private final IDespachoPollitosService service;

    @RequirePlantaIncubacion
    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.getAllDespachosPollitos());
    }
}