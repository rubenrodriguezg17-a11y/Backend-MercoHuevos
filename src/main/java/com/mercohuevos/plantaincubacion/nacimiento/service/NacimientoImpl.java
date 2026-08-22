package com.mercohuevos.plantaincubacion.nacimiento.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import com.mercohuevos.plantaincubacion.nacimiento.repository.IDetalleNacimientoLoteRepository;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaLoteResumenDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.ICargaService;
import com.mercohuevos.plantaincubacion.nacimiento.dto.*;
import com.mercohuevos.plantaincubacion.nacimiento.model.ClasificacionPollitos;
import com.mercohuevos.plantaincubacion.nacimiento.model.DetalleNacimientoLote;
import com.mercohuevos.plantaincubacion.nacimiento.model.Nacimiento;
import com.mercohuevos.plantaincubacion.nacimiento.repository.INacimientoRepository;
import com.mercohuevos.plantaincubacion.transferencia.dto.DetalleTransferenciaInfoDTO;
import com.mercohuevos.plantaincubacion.transferencia.service.ITransferenciaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NacimientoImpl implements INacimientoService {

    private final INacimientoRepository nacimientoRepo;
    private final ICargaService cargaService;
    private final ITransferenciaService transferenciaService;
    private final IDetalleNacimientoLoteRepository detalleRepo;

    @Override
    @Transactional
    public NacimientoResponseDTO registrar(Long idCarga, RegistrarNacimientoRequestDTO request) {
        if (nacimientoRepo.findByIdCarga(idCarga).isPresent()) {
            throw new IllegalStateException("Esta carga ya tiene un registro de nacimiento");
        }

        Nacimiento nacimiento = new Nacimiento();
        nacimiento.setIdCarga(idCarga);
        nacimiento.setFechaNacimiento(request.fechaNacimiento());
        nacimiento.setResponsable(request.responsable());

        for (DetalleNacimientoLoteRequestDTO detalleReq : request.detalleNacimiento()) {
            DetalleTransferenciaInfoDTO transferenciaInfo =
                    transferenciaService.obtenerDetallePorId(detalleReq.idDetalleTransferencia());

            if (!transferenciaInfo.idCarga().equals(idCarga)) {
                throw new IllegalArgumentException(
                        "El detalle de transferencia " + detalleReq.idDetalleTransferencia() + " no pertenece a la carga " + idCarga);
            }
            if (Boolean.TRUE.equals(transferenciaInfo.liberado())) {
                throw new IllegalStateException(
                        "El detalle de transferencia " + detalleReq.idDetalleTransferencia() + " ya fue liquidado en un nacimiento");
            }

            int huevosTransferidos = transferenciaInfo.huevosTransferidos();
            ClasificacionPollitosRequestDTO c = detalleReq.clasificacion();
            int suma = detalleReq.noNacidos() + c.pollitosDescarte()
                    + c.machosPrimera() + c.machosSegunda() + c.hembrasPrimera() + c.hembrasSegunda();

            if (suma != huevosTransferidos) {
                throw new IllegalArgumentException(
                        "El balance no cuadra (detalle transferencia " + detalleReq.idDetalleTransferencia() +
                                "): huevos transferidos=" + huevosTransferidos + ", suma de resultados=" + suma);
            }

            DetalleNacimientoLote detalle = new DetalleNacimientoLote();
            detalle.setNacimiento(nacimiento);
            detalle.setIdDetalleTransferencia(detalleReq.idDetalleTransferencia());
            detalle.setIdCargaLote(transferenciaInfo.idCargaLote());
            detalle.setHuevosTransferidos(huevosTransferidos);
            detalle.setNoNacidos(detalleReq.noNacidos());

            ClasificacionPollitos clasificacion = new ClasificacionPollitos();
            clasificacion.setDetalleNacimiento(detalle);
            clasificacion.setMachosPrimera(c.machosPrimera());
            clasificacion.setMachosSegunda(c.machosSegunda());
            clasificacion.setHembrasPrimera(c.hembrasPrimera());
            clasificacion.setHembrasSegunda(c.hembrasSegunda());
            clasificacion.setPollitosDescarte(c.pollitosDescarte());
            detalle.setClasificacion(clasificacion);

            nacimiento.getDetalles().add(detalle);
        }

        nacimientoRepo.save(nacimiento);

        for (DetalleNacimientoLoteRequestDTO detalleReq : request.detalleNacimiento()) {
            transferenciaService.liberarDetalle(detalleReq.idDetalleTransferencia());
        }

        cargaService.cambiarEstado(idCarga, EstadoCarga.EN_VACUNACION);

        return construirResponse(nacimiento);
    }

    @Override
    public NacimientoResponseDTO obtenerPorCarga(Long idCarga) {
        Nacimiento nacimiento = nacimientoRepo.findByIdCarga(idCarga)
                .orElseThrow(() -> new EntityNotFoundException("Esta carga aun no tiene registro de nacimiento"));
        return construirResponse(nacimiento);
    }

    @Override
    public ClasificacionDisponibleDTO obtenerClasificacionPorDetalle(Long idDetalleNacimiento) {
        DetalleNacimientoLote detalle = detalleRepo.findById(idDetalleNacimiento)
                .orElseThrow(()-> new EntityNotFoundException("Detalle nacimiento no encontrado: " + idDetalleNacimiento));
        ClasificacionPollitos c = detalle.getClasificacion();
        CargaLoteResumenDTO lote = cargaService.obtenerLote(detalle.getIdCargaLote());

        return new ClasificacionDisponibleDTO(
                detalle.getIdDetalleNacimiento(),
                detalle.getIdCargaLote(),
                lote.codigoFusion(),
                c.getMachosPrimera(),
                c.getMachosSegunda(),
                c.getHembrasPrimera(),
                c.getHembrasSegunda()
        );
    }

    @Override
    public List<NacimientoResponseDTO> getAllNacimientos() {
        return nacimientoRepo.findAll().stream()
                .sorted(Comparator.comparing(Nacimiento::getFechaNacimiento).reversed())
                .map(this::construirResponse)
                .toList();
    }

    private NacimientoResponseDTO construirResponse(Nacimiento nacimiento) {
        List<DetalleNacimientoLoteResponseDTO> detallesDTO = nacimiento.getDetalles().stream()
                .map(this::construirDetalleResponse)
                .toList();

        return new NacimientoResponseDTO(
                nacimiento.getIdNacimiento(), nacimiento.getIdCarga(),
                nacimiento.getFechaNacimiento(), nacimiento.getResponsable(), detallesDTO
        );
    }

    private DetalleNacimientoLoteResponseDTO construirDetalleResponse(DetalleNacimientoLote detalle) {
        int totalNacidos = detalle.getHuevosTransferidos() - detalle.getNoNacidos();
        ClasificacionPollitos c = detalle.getClasificacion();
        int totalViables = c.getMachosPrimera() + c.getMachosSegunda() + c.getHembrasPrimera() + c.getHembrasSegunda();

        BigDecimal porcentajeNacimiento = calcularPorcentaje(totalNacidos, detalle.getHuevosTransferidos());
        BigDecimal porcentajeAprovechamiento = calcularPorcentaje(totalViables, detalle.getHuevosTransferidos());

        CargaLoteResumenDTO lote = cargaService.obtenerLote(detalle.getIdCargaLote());

        ClasificacionPollitosResponseDTO clasificacionDTO = new ClasificacionPollitosResponseDTO(
                c.getMachosPrimera(), c.getMachosSegunda(), c.getHembrasPrimera(), c.getHembrasSegunda(),
                c.getPollitosDescarte(), totalViables
        );

        return new DetalleNacimientoLoteResponseDTO(
                detalle.getIdDetalleNacimiento(), detalle.getIdCargaLote(), lote.codigoFusion(),
                detalle.getHuevosTransferidos(), detalle.getNoNacidos(), totalNacidos,
                porcentajeNacimiento, porcentajeAprovechamiento, clasificacionDTO
        );
    }

    private BigDecimal calcularPorcentaje(int cantidad, int total) {
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(cantidad)
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}