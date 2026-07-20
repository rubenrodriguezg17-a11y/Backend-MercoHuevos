package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.granja.dto.LoteDTO;
import com.mercohuevos.granja.dto.LoteRequestDTO;

public interface ILoteService {
    LoteDTO crear(LoteRequestDTO request);
    LoteDTO obtenerPorId(Long id);
    List<LoteDTO> listarTodos();
    LoteDTO editar(Long id, LoteRequestDTO request);
    void darDeBaja(Long id);
    Integer calcularPoblacionActual(Long idLote);
}