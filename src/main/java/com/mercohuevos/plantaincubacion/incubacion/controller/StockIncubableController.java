package com.mercohuevos.plantaincubacion.incubacion.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.incubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.StockIncubableConsultaDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.IStockIncubableService;

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
    
    @GetMapping("/consulta")
    public ResponseEntity<StockIncubableConsultaDTO> consultarPorFecha(
            @RequestParam LocalDate fecha) {
        return ResponseEntity.ok(service.consultarPorFecha(fecha));
    }
    
    @PostMapping("/pasar-a-carton")
    public ResponseEntity<StockIncubableDTO> pasarACarton(@Valid @RequestBody PasarACartonRequestDTO request) {
        return ResponseEntity.ok(service.pasarACarton(request));
    }
}