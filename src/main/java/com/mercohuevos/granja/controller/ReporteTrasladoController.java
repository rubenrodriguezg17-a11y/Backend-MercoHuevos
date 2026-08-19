package com.mercohuevos.granja.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.granja.dto.ReporteTrasladoRequestDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoResponseDTO;
import com.mercohuevos.granja.service.IReporteTrasladoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/granja/reportes")
@RequiredArgsConstructor
public class ReporteTrasladoController {

    private final IReporteTrasladoService reporteService;

    @RequireGranja
    @PostMapping
    public ResponseEntity<ReporteTrasladoResponseDTO> crear(
            @Valid
            @RequestBody 
            ReporteTrasladoRequestDTO request) {

        ReporteTrasladoResponseDTO creado = reporteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @RequireLecturaTraslado
    @GetMapping("/{id}")
    public ResponseEntity<ReporteTrasladoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @RequireLecturaTraslado
    @GetMapping
    public ResponseEntity<List<ReporteTrasladoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @RequireGranja
    @PutMapping("/{id}")
    public ResponseEntity<ReporteTrasladoResponseDTO> editar(
            @PathVariable Long id, @Valid @RequestBody ReporteTrasladoRequestDTO request) {
        return ResponseEntity.ok(reporteService.editar(id, request));
    }
    @RequireGranja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        reporteService.anular(id);
        return ResponseEntity.noContent().build();
    }
    @RequireLecturaTraslado
    @GetMapping("/semana")
    public ResponseEntity<List<ReporteTrasladoResponseDTO>> listarPorSemana(
            @RequestParam(defaultValue = "0") int semanasAtras) {
        return ResponseEntity.ok(reporteService.listarPorSemana(semanasAtras));
    }
}