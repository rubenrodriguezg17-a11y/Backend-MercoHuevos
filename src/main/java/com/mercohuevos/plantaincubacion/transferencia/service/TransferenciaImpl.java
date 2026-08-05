package com.mercohuevos.plantaincubacion.transferencia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.EstadoMaquina;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaLoteResumenDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.ICargaService;
import com.mercohuevos.plantaincubacion.incubacion.service.IMirajeService;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.shared.repository.IMaquinaRepository;
import com.mercohuevos.plantaincubacion.transferencia.dto.*;
import com.mercohuevos.plantaincubacion.transferencia.model.DetalleTransferenciaLote;
import com.mercohuevos.plantaincubacion.transferencia.model.Transferencia;
import com.mercohuevos.plantaincubacion.transferencia.repository.IDetalleTransferenciaLoteRepository;
import com.mercohuevos.plantaincubacion.transferencia.repository.ITransferenciaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferenciaImpl implements ITransferenciaService {

    private final ITransferenciaRepository transferenciaRepo;
    private final IDetalleTransferenciaLoteRepository detalleRepo;
    private final IMaquinaRepository maquinaRepo;
    private final ICargaService cargaService;
    private final IMirajeService mirajeService;

    @Override
    @Transactional
    public TransferenciaResponseDTO registrar(Long idCarga, RegistrarTransferenciaRequestDTO request) {
        CargaDetalleResponseDTO carga = cargaService.obtenerPorId(idCarga);

        if (!"EN_INCUBACION".equals(carga.estado())) {
            throw new IllegalStateException(
                    "Solo se puede transferir una carga que este EN_INCUBACION. Estado actual: " + carga.estado());
        }
        if (request.fechaTransferencia().isBefore(carga.fechaTransferencia())) {
            throw new IllegalArgumentException(
                    "La transferencia no puede registrarse antes del dia 18 (" + carga.fechaTransferencia() + ")");
        }

        List<CargaLoteResumenDTO> lotesDeCarga = cargaService.listarLotesPorCarga(idCarga);

        Transferencia transferencia = new Transferencia();
        transferencia.setIdCarga(idCarga);
        transferencia.setFechaTransferencia(request.fechaTransferencia());
        transferencia.setResponsable(request.responsable());

        for (DetalleTransferenciaLoteRequestDTO detalleReq : request.detalleTransferencia()) {
            CargaLoteResumenDTO lote = lotesDeCarga.stream()
                    .filter(l -> l.idCargaLote().equals(detalleReq.idCargaLote()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El lote " + detalleReq.idCargaLote() + " no pertenece a la carga " + idCarga));

            Maquina origen = maquinaRepo.findById(detalleReq.idMaquinaOrigen())
                    .orElseThrow(() -> new EntityNotFoundException("Maquina origen no encontrada: " + detalleReq.idMaquinaOrigen()));
            if (origen.getTipo() != TipoMaquina.INCUBADORA) {
                throw new IllegalArgumentException("La maquina origen " + origen.getNumero() + " no es una incubadora");
            }

            Maquina nacedora = maquinaRepo.findById(detalleReq.idNacedoraDestino())
                    .orElseThrow(() -> new EntityNotFoundException("Nacedora no encontrada: " + detalleReq.idNacedoraDestino()));
            if (nacedora.getTipo() != TipoMaquina.NACEDORA) {
                throw new IllegalArgumentException("La maquina destino " + nacedora.getNumero() + " no es una nacedora");
            }

            int huevosViables = mirajeService.obtenerHuevosViablesPorCargaLote(detalleReq.idCargaLote());
            int yaTransferido = detalleRepo.sumHuevosTransferidosPorCargaLote(detalleReq.idCargaLote());
            int disponible = huevosViables - yaTransferido;

            if (detalleReq.huevosTransferidos() > disponible) {
                throw new IllegalArgumentException(
                        "El lote " + lote.codigoFusion() + " solo tiene " + disponible + " huevos viables disponibles para transferir");
            }

            int ocupacionNacedora = detalleRepo.sumHuevosTransferidosActivosPorNacedora(nacedora);
            if (ocupacionNacedora + detalleReq.huevosTransferidos() > nacedora.getCapacidadMaxima()) {
                throw new IllegalArgumentException(
                        "La nacedora " + nacedora.getNumero() + " no tiene capacidad suficiente. Disponible: " +
                                (nacedora.getCapacidadMaxima() - ocupacionNacedora) + ", solicitado: " + detalleReq.huevosTransferidos());
            }

            DetalleTransferenciaLote detalle = new DetalleTransferenciaLote();
            detalle.setTransferencia(transferencia);
            detalle.setIdCargaLote(detalleReq.idCargaLote());
            detalle.setMaquinaOrigen(origen);
            detalle.setNacedoraDestino(nacedora);
            detalle.setHuevosTransferidos(detalleReq.huevosTransferidos());
            detalle.setLiberado(false);
            transferencia.getDetalles().add(detalle);

            if (nacedora.getEstado() != EstadoMaquina.EN_USO) {
                nacedora.setEstado(EstadoMaquina.EN_USO);
                maquinaRepo.save(nacedora);
            }
        }

        transferenciaRepo.save(transferencia);

        if (todosLosLotesFueronTransferidos(lotesDeCarga)) {
            cargaService.cambiarEstado(idCarga, EstadoCarga.EN_NACEDORA);
            liberarIncubadorasDeOrigen(idCarga);
        }

        return construirResponse(transferencia);
    }

    private boolean todosLosLotesFueronTransferidos(List<CargaLoteResumenDTO> lotes) {
        for (CargaLoteResumenDTO lote : lotes) {
            int viables = mirajeService.obtenerHuevosViablesPorCargaLote(lote.idCargaLote());
            int transferido = detalleRepo.sumHuevosTransferidosPorCargaLote(lote.idCargaLote());
            if (transferido < viables) return false;
        }
        return true;
    }

    private void liberarIncubadorasDeOrigen(Long idCarga) {
        for (Maquina incubadora : detalleRepo.findMaquinasOrigenPorCarga(idCarga)) {
            incubadora.setEstado(EstadoMaquina.DESINFECCION);
            maquinaRepo.save(incubadora);
        }
    }

    @Override
    public List<TransferenciaResponseDTO> listarPorCarga(Long idCarga) {
        return transferenciaRepo.findByIdCargaOrderByFechaTransferenciaAsc(idCarga).stream()
                .map(this::construirResponse)
                .toList();
    }

    @Override
    public List<LoteDisponibleTransferenciaDTO> listarLotesDisponibles(Long idCarga) {
        return cargaService.listarLotesPorCarga(idCarga).stream()
                .map(lote -> {
                    int viables = mirajeService.obtenerHuevosViablesPorCargaLote(lote.idCargaLote());
                    int transferido = detalleRepo.sumHuevosTransferidosPorCargaLote(lote.idCargaLote());
                    return new LoteDisponibleTransferenciaDTO(
                            lote.idCargaLote(), lote.idFusionLote(), lote.codigoFusion(), viables - transferido);
                })
                .filter(dto -> dto.huevosViablesDisponibles() > 0)
                .toList();
    }

    @Override
    public List<NacedoraDisponibleDTO> listarNacedorasDisponibles() {
        return maquinaRepo.findByTipo(TipoMaquina.NACEDORA).stream()
                .map(nacedora -> {
                    int ocupacion = detalleRepo.sumHuevosTransferidosActivosPorNacedora(nacedora);
                    return new NacedoraDisponibleDTO(
                            nacedora.getIdMaquina(), nacedora.getNumero(), nacedora.getCapacidadMaxima(),
                            nacedora.getCapacidadMaxima() - ocupacion, nacedora.getEstado().name());
                })
                .toList();
    }

    @Override
    public DetalleTransferenciaInfoDTO obtenerDetallePorId(Long idDetalleTransferencia) {
        DetalleTransferenciaLote detalle = buscarDetalle(idDetalleTransferencia);
        return new DetalleTransferenciaInfoDTO(
                detalle.getIdDetalleTransferencia(), detalle.getTransferencia().getIdCarga(),
                detalle.getIdCargaLote(), detalle.getHuevosTransferidos(), detalle.getLiberado()
        );
    }

    @Override
    @Transactional
    public void liberarDetalle(Long idDetalleTransferencia) {
        DetalleTransferenciaLote detalle = buscarDetalle(idDetalleTransferencia);
        detalle.setLiberado(true);
        detalleRepo.save(detalle);

        Maquina nacedora = detalle.getNacedoraDestino();
        if (detalleRepo.sumHuevosTransferidosActivosPorNacedora(nacedora) == 0) {
            nacedora.setEstado(EstadoMaquina.DESINFECCION);
            maquinaRepo.save(nacedora);
        }
    }

    private DetalleTransferenciaLote buscarDetalle(Long id) {
        return detalleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de transferencia no encontrado: " + id));
    }

    private TransferenciaResponseDTO construirResponse(Transferencia transferencia) {
        List<DetalleTransferenciaLoteResponseDTO> detallesDTO = transferencia.getDetalles().stream()
                .map(d -> {
                    CargaLoteResumenDTO lote = cargaService.obtenerLote(d.getIdCargaLote());
                    return new DetalleTransferenciaLoteResponseDTO(
                            d.getIdDetalleTransferencia(), d.getIdCargaLote(), lote.codigoFusion(),
                            d.getMaquinaOrigen().getIdMaquina(), d.getMaquinaOrigen().getNumero(),
                            d.getNacedoraDestino().getIdMaquina(), d.getNacedoraDestino().getNumero(),
                            d.getHuevosTransferidos()
                    );
                })
                .toList();

        return new TransferenciaResponseDTO(
                transferencia.getIdTransferencia(), transferencia.getIdCarga(),
                transferencia.getFechaTransferencia(), transferencia.getResponsable(), detallesDTO
        );
    }
}