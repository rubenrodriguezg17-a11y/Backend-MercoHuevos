package com.mercohuevos.granja.controller;

import java.time.LocalDate;
import java.util.List;

import com.mercohuevos.auth.annotation.*;
import com.mercohuevos.common.dto.PaginaResponseDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoFiltroDTO;
import com.mercohuevos.granja.enums.EstadoReporte;
import org.springframework.format.annotation.DateTimeFormat;
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
    @GetMapping("/paginado")
    public ResponseEntity<PaginaResponseDTO<ReporteTrasladoResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) EstadoReporte estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String chofer) {

        ReporteTrasladoFiltroDTO filtro = new ReporteTrasladoFiltroDTO(estado, fechaInicio, fechaFin, chofer);
        return ResponseEntity.ok(reporteService.listarPaginado(page, size, filtro));
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