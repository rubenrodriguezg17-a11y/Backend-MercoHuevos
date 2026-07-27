package com.mercohuevos.plantaincubacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.service.IStockIncubableService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/stock-incubable")
@RequiredArgsConstructor
public class StockIncubableController {

    private final IStockIncubableService service;

    @GetMapping
    public ResponseEntity<List<StockIncubableDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/fusion-lote/{idFusionLote}")
    public ResponseEntity<List<StockIncubableDTO>> listarPorFusionLote(@PathVariable Long idFusionLote) {
        return ResponseEntity.ok(service.listarPorFusionLote(idFusionLote));
    }
    
    @PostMapping("/pasar-a-carton")
    public ResponseEntity<StockIncubableDTO> pasarACarton(@Valid @RequestBody PasarACartonRequestDTO request) {
        return ResponseEntity.ok(service.pasarACarton(request));
    }
}