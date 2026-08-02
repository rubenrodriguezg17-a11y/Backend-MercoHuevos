package com.mercohuevos.plantaincubacion.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.dto.EditarFusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.service.IFusionLoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/fusiones")
@RequiredArgsConstructor
public class FusionLoteController {

    private final IFusionLoteService fusionLoteService;

    @PostMapping
    public ResponseEntity<FusionLoteDTO> crear(@Valid @RequestBody FusionLoteRequestDTO request) {
        return ResponseEntity.ok(fusionLoteService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FusionLoteDTO> editar(@PathVariable Long id, @Valid @RequestBody EditarFusionLoteRequestDTO request) {
        return ResponseEntity.ok(fusionLoteService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        fusionLoteService.anular(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recepcion/{idRecepcion}/activas")
    public ResponseEntity<List<FusionLoteDTO>> listarActivasPorRecepcion(@PathVariable Long idRecepcion) {
        return ResponseEntity.ok(fusionLoteService.listarActivasPorRecepcion(idRecepcion));
    }

    @GetMapping("/recepcion/{idRecepcion}/anuladas")
    public ResponseEntity<List<FusionLoteDTO>> listarAnuladasPorRecepcion(@PathVariable Long idRecepcion) {
        return ResponseEntity.ok(fusionLoteService.listarAnuladasPorRecepcion(idRecepcion));
    }

    @GetMapping("/recepcion/{idRecepcion}/todas")
    public ResponseEntity<List<FusionLoteDTO>> listarTodasPorRecepcion(@PathVariable Long idRecepcion) {
        return ResponseEntity.ok(fusionLoteService.listarTodasPorRecepcion(idRecepcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FusionLoteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fusionLoteService.obtenerPorId(id));
    }
    
}