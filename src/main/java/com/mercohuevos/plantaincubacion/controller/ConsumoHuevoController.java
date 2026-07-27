package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoResumenDTO;
import com.mercohuevos.plantaincubacion.service.IConsumoHuevoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/consumo-huevo")
@RequiredArgsConstructor
public class ConsumoHuevoController {

    private final IConsumoHuevoService service;

    @GetMapping
    public ResponseEntity<List<ConsumoHuevoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/fusion-lote/{idFusionLote}")
    public ResponseEntity<List<ConsumoHuevoDTO>> listarPorFusionLote(@PathVariable Long idFusionLote) {
        return ResponseEntity.ok(service.listarPorFusionLote(idFusionLote));
    }
    
    @GetMapping("/resumen/{idFusionLote}")
    public ResponseEntity<ConsumoHuevoResumenDTO> obtenerResumen(@PathVariable Long idFusionLote) {
        return ResponseEntity.ok(service.obtenerResumenPorFusionLote(idFusionLote));
    }
}