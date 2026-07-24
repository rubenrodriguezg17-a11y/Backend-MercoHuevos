package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteRequestDTO;
import com.mercohuevos.plantaincubacion.mapper.IFusionLoteMapper;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.FusionLoteMiembro;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteMiembroRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.ILoteOrigenReporteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FusionLoteImpl implements IFusionLoteService {

    private final IFusionLoteRepository fusionLoteRepo;
    private final IFusionLoteMiembroRepository miembroRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
    private final IFusionLoteMapper mapper;

    @Override
    public FusionLoteDTO crear(FusionLoteRequestDTO request) {

        validarCodigosNoMapeados(request.codigosLoteGranja());

        String lineaGenetica = validarYObtenerLineaGeneticaUnica(request.codigosLoteGranja());

        FusionLote fusionLote = new FusionLote();
        fusionLote.setNombre(request.nombre());
        fusionLote.setLineaGeneticaNombre(lineaGenetica);
        fusionLote.setFechaCreacion(LocalDate.now());
        fusionLote.setActivo(true);

        FusionLote guardada = fusionLoteRepo.save(fusionLote);

        for (String codigo : request.codigosLoteGranja()) {
            FusionLoteMiembro miembro = new FusionLoteMiembro();
            miembro.setFusionLote(guardada);
            miembro.setCodigoLoteGranja(codigo);
            miembroRepo.save(miembro);
        }

        return construirDTOCompleto(guardada);
    }

    @Override
    public FusionLoteDTO obtenerPorId(Long id) {
        return construirDTOCompleto(buscarFusionLote(id));
    }

    @Override
    public List<FusionLoteDTO> listarTodos() {
        return fusionLoteRepo.findAll().stream()
                .map(this::construirDTOCompleto)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        FusionLote fusionLote = buscarFusionLote(id);
        List<FusionLoteMiembro> miembros = miembroRepo.findByFusionLote(fusionLote);
        miembroRepo.deleteAll(miembros);
        fusionLoteRepo.delete(fusionLote);
    }

    private void validarCodigosNoMapeados(List<String> codigos) {
        for (String codigo : codigos) {
            if (miembroRepo.existsByCodigoLoteGranja(codigo)) {
                throw new IllegalArgumentException(
                        "El lote " + codigo + " ya pertenece a otra fusion");
            }
        }
    }

    private String validarYObtenerLineaGeneticaUnica(List<String> codigos) {
        List<LoteOrigenReporte> lotesOrigen = loteOrigenRepo.findByCodigoLoteGranjaIn(codigos);

        if (lotesOrigen.size() != codigos.size()) {
            throw new IllegalArgumentException(
                    "Uno o mas codigos de lote no corresponden a ningun reporte recibido de granja");
        }

        Set<String> lineasDistintas = lotesOrigen.stream()
                .map(LoteOrigenReporte::getLineaGeneticaNombre)
                .collect(Collectors.toSet());

        if (lineasDistintas.size() > 1) {
            throw new IllegalArgumentException(
                    "No se pueden fusionar lotes de distinta linea genetica: " + lineasDistintas);
        }

        return lineasDistintas.iterator().next();
    }

    private FusionLoteDTO construirDTOCompleto(FusionLote fusionLote) {
        FusionLoteDTO base = mapper.toDTO(fusionLote);

        List<String> codigos = miembroRepo.findByFusionLote(fusionLote).stream()
                .map(FusionLoteMiembro::getCodigoLoteGranja)
                .toList();

        return new FusionLoteDTO(
                base.idFusionLote(), base.nombre(), base.lineaGeneticaNombre(),
                base.fechaCreacion(), codigos
        );
    }

    private FusionLote buscarFusionLote(Long id) {
        return fusionLoteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fusion de lote no encontrada: " + id));
    }
}