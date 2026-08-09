package com.mercohuevos.plantaincubacion.incubacion.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireAdmin;
import com.mercohuevos.auth.annotation.RequireEmbandejado;
import com.mercohuevos.auth.annotation.RequireLecturaEmbandejado;
import com.mercohuevos.auth.annotation.RequirePlantaIncubacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoResumenDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.IConsumoHuevoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/consumo-huevo")
@RequiredArgsConstructor
public class ConsumoHuevoController {

    private final IConsumoHuevoService service;

    @RequireLecturaEmbandejado
    @GetMapping
    public ResponseEntity<List<ConsumoHuevoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @RequireLecturaEmbandejado
    @GetMapping("/fusion-lote/{idFusionLote}")
    public ResponseEntity<List<ConsumoHuevoDTO>> listarPorFusionLote(@PathVariable Long idFusionLote) {
        return ResponseEntity.ok(service.listarPorFusionLote(idFusionLote));
    }

    @RequireLecturaEmbandejado
    @GetMapping("/resumen/{idFusionLote}")
    public ResponseEntity<ConsumoHuevoResumenDTO> obtenerResumen(@PathVariable Long idFusionLote) {
        return ResponseEntity.ok(service.obtenerResumenPorFusionLote(idFusionLote));
    }
}