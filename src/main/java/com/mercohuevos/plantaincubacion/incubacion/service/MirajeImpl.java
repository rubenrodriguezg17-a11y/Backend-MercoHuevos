package com.mercohuevos.plantaincubacion.incubacion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.incubacion.dto.DetalleMirajeLoteRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.DetalleMirajeLoteResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.MirajeResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.RegistrarMirajeRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.CargaLote;
import com.mercohuevos.plantaincubacion.incubacion.model.DetalleMirajeLote;
import com.mercohuevos.plantaincubacion.incubacion.model.Miraje;
import com.mercohuevos.plantaincubacion.incubacion.repository.ICargaLoteRepository;
import com.mercohuevos.plantaincubacion.incubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.incubacion.repository.IDetalleMirajeLoteRepository;
import com.mercohuevos.plantaincubacion.incubacion.repository.IMirajeRepository;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.shared.repository.IMaquinaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MirajeImpl implements IMirajeService {

    private final IMirajeRepository mirajeRepo;
    private final IDetalleMirajeLoteRepository detalleRepo;
    private final ICargaRepository cargaRepo;
    private final ICargaLoteRepository cargaLoteRepo;
    private final IMaquinaRepository maquinaRepo;

    @Override
    @Transactional
    public MirajeResponseDTO registrar(Long idCarga, RegistrarMirajeRequestDTO request) {
        Carga carga = buscarCarga(idCarga);

        if (carga.getEstado() != EstadoCarga.EN_INCUBACION) {
            throw new IllegalStateException(
                    "Solo se puede registrar miraje sobre una carga que este EN_INCUBACION. Estado actual: " + carga.getEstado());
        }

        Miraje miraje = new Miraje();
        miraje.setCarga(carga);
        miraje.setFechaMiraje(request.fechaMiraje());
        miraje.setResponsable(request.responsable());

        for (DetalleMirajeLoteRequestDTO detalleReq : request.detalleMiraje()) {
            CargaLote cargaLote = cargaLoteRepo.findById(detalleReq.idCargaLote())
                    .orElseThrow(() -> new EntityNotFoundException("CargaLote no encontrado: " + detalleReq.idCargaLote()));

            if (!cargaLote.getCarga().getIdCarga().equals(idCarga)) {
                throw new IllegalArgumentException(
                        "El lote " + detalleReq.idCargaLote() + " no pertenece a la carga " + idCarga);
            }

            Maquina maquina = maquinaRepo.findById(detalleReq.idMaquina())
                    .orElseThrow(() -> new EntityNotFoundException("Maquina no encontrada: " + detalleReq.idMaquina()));

            Integer huevosCargados = cargaLote.getCantidadInicial();
            int yaRegistrado = detalleRepo.sumHuevosInfertilesPorCargaLote(cargaLote);
            int nuevoAcumulado = yaRegistrado + detalleReq.huevosInfertiles();

            if (nuevoAcumulado > huevosCargados) {
                throw new IllegalArgumentException(
                        "El acumulado de infertiles (" + nuevoAcumulado + ") no puede superar los huevos cargados (" +
                                huevosCargados + ") del lote " + cargaLote.getFusionLote().getCodigoFusion() + ". Ya registrado: " + yaRegistrado);
            }

            DetalleMirajeLote detalle = new DetalleMirajeLote();
            detalle.setMiraje(miraje);
            detalle.setCargaLote(cargaLote);
            detalle.setMaquina(maquina);
            detalle.setHuevosCargados(huevosCargados);
            detalle.setHuevosInfertiles(detalleReq.huevosInfertiles());

            miraje.getDetalles().add(detalle);
        }

        mirajeRepo.save(miraje);
        return construirResponse(miraje);
    }

    @Override
    public List<MirajeResponseDTO> listarPorCarga(Long idCarga) {
        Carga carga = buscarCarga(idCarga);
        return mirajeRepo.findByCargaOrderByFechaMirajeAsc(carga).stream()
                .map(this::construirResponse)
                .toList();
    }

    @Override
    public Integer obtenerHuevosViablesPorCargaLote(Long idCargaLote) {
        CargaLote cargaLote = cargaLoteRepo.findById(idCargaLote)
                .orElseThrow(() -> new EntityNotFoundException("CargaLote no encontrado: " + idCargaLote));
        int infertiles = detalleRepo.sumHuevosInfertilesPorCargaLote(cargaLote);
        return cargaLote.getCantidadInicial() - infertiles;
    }

    private MirajeResponseDTO construirResponse(Miraje miraje) {
        List<DetalleMirajeLoteResponseDTO> detallesDTO = miraje.getDetalles().stream()
                .map(this::construirDetalleResponse)
                .toList();

        return new MirajeResponseDTO(
                miraje.getIdMiraje(), miraje.getCarga().getIdCarga(),
                miraje.getFechaMiraje(), miraje.getResponsable(), detallesDTO
        );
    }

    private DetalleMirajeLoteResponseDTO construirDetalleResponse(DetalleMirajeLote detalle) {
        int huevosViables = detalle.getHuevosCargados() - detalle.getHuevosInfertiles();

        BigDecimal porcentaje = detalle.getHuevosCargados() == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(detalle.getHuevosInfertiles())
                .divide(BigDecimal.valueOf(detalle.getHuevosCargados()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return new DetalleMirajeLoteResponseDTO(
                detalle.getIdDetalleMiraje(), detalle.getCargaLote().getIdCargaLote(),
                detalle.getCargaLote().getFusionLote().getCodigoFusion(),
                detalle.getMaquina().getIdMaquina(), detalle.getMaquina().getNumero(),
                detalle.getHuevosCargados(), detalle.getHuevosInfertiles(),
                huevosViables, porcentaje
        );
    }

    private Carga buscarCarga(Long id) {
        return cargaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + id));
    }
}