package com.mercohuevos.plantaincubacion.vacunacion.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.incubacion.service.ICargaService;
import com.mercohuevos.plantaincubacion.nacimiento.dto.ClasificacionDisponibleDTO;
import com.mercohuevos.plantaincubacion.nacimiento.service.INacimientoService;
import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;
import com.mercohuevos.plantaincubacion.shared.repository.IClienteRepository;
import com.mercohuevos.plantaincubacion.shared.repository.ITipoVacunaRepository;
import com.mercohuevos.plantaincubacion.vacunacion.dto.*;
import com.mercohuevos.plantaincubacion.vacunacion.model.DetalleVacunacionClienteLote;
import com.mercohuevos.plantaincubacion.vacunacion.model.OrdenVacunacion;
import com.mercohuevos.plantaincubacion.vacunacion.repository.IDetalleVacunacionClienteLoteRepository;
import com.mercohuevos.plantaincubacion.vacunacion.repository.IOrdenVacunacionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenVacunacionImpl implements IOrdenVacunacionService {

    private final IOrdenVacunacionRepository ordenRepo;
    private final IDetalleVacunacionClienteLoteRepository detalleRepo;
    private final IClienteRepository clienteRepo;
    private final ITipoVacunaRepository tipoVacunaRepo;
    private final INacimientoService nacimientoService;
    private final ICargaService cargaService;

    @Override
    @Transactional
    public OrdenVacunacionResponseDTO registrar(Long idCarga, RegistrarOrdenVacunacionRequestDTO request) {
        cargaService.obtenerPorId(idCarga); // valida que la carga exista

        OrdenVacunacion orden = new OrdenVacunacion();
        orden.setIdCarga(idCarga);
        orden.setFechaVacunacion(request.fechaVacunacion());
        orden.setResponsableVacunacion(request.responsableVacunacion());

        Map<Long, List<DetalleVacunacionClienteLoteRequestDTO>> porLote = request.detalleVacunacion().stream()
                .collect(Collectors.groupingBy(DetalleVacunacionClienteLoteRequestDTO::idDetalleNacimiento));

        for (Map.Entry<Long, List<DetalleVacunacionClienteLoteRequestDTO>> entry : porLote.entrySet()) {
            Long idDetalleNacimiento = entry.getKey();
            List<DetalleVacunacionClienteLoteRequestDTO> detallesDelLote = entry.getValue();

            if (detalleRepo.existsByIdDetalleNacimiento(idDetalleNacimiento)) {
                throw new IllegalStateException(
                        "El detalle de nacimiento " + idDetalleNacimiento + " ya fue vacunado anteriormente");
            }

            ClasificacionDisponibleDTO disponible = nacimientoService.obtenerClasificacionPorDetalle(idDetalleNacimiento);

            validarNoSupera("machos 1ra", sumar(detallesDelLote, DetalleVacunacionClienteLoteRequestDTO::machos1raVacunados),
                    disponible.machosPrimera(), disponible.codigoFusion());
            validarNoSupera("machos 2da", sumar(detallesDelLote, DetalleVacunacionClienteLoteRequestDTO::machos2daVacunados),
                    disponible.machosSegunda(), disponible.codigoFusion());
            validarNoSupera("hembras 1ra", sumar(detallesDelLote, DetalleVacunacionClienteLoteRequestDTO::hembras1raVacunadas),
                    disponible.hembrasPrimera(), disponible.codigoFusion());
            validarNoSupera("hembras 2da", sumar(detallesDelLote, DetalleVacunacionClienteLoteRequestDTO::hembras2daVacunadas),
                    disponible.hembrasSegunda(), disponible.codigoFusion());

            for (DetalleVacunacionClienteLoteRequestDTO detalleReq : detallesDelLote) {
                Cliente cliente = clienteRepo.findById(detalleReq.idCliente())
                        .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + detalleReq.idCliente()));
                TipoVacuna vacuna = tipoVacunaRepo.findById(detalleReq.idTipoVacuna())
                        .orElseThrow(() -> new EntityNotFoundException("Tipo de vacuna no encontrado: " + detalleReq.idTipoVacuna()));

                DetalleVacunacionClienteLote detalle = new DetalleVacunacionClienteLote();
                detalle.setOrdenVacunacion(orden);
                detalle.setIdDetalleNacimiento(idDetalleNacimiento);
                detalle.setCliente(cliente);
                detalle.setTipoVacuna(vacuna);
                detalle.setMachos1raVacunados(detalleReq.machos1raVacunados());
                detalle.setMachos2daVacunados(detalleReq.machos2daVacunados());
                detalle.setHembras1raVacunadas(detalleReq.hembras1raVacunadas());
                detalle.setHembras2daVacunadas(detalleReq.hembras2daVacunadas());

                orden.getDetalles().add(detalle);
            }
        }

        ordenRepo.save(orden);
        return construirResponse(orden);
    }

    private int sumar(List<DetalleVacunacionClienteLoteRequestDTO> detalles,
                      java.util.function.ToIntFunction<DetalleVacunacionClienteLoteRequestDTO> extractor) {
        return detalles.stream().mapToInt(extractor).sum();
    }

    private void validarNoSupera(String etiqueta, int solicitado, int disponible, String codigoFusion) {
        if (solicitado > disponible) {
            throw new IllegalArgumentException(
                    "El lote " + codigoFusion + " no tiene suficientes " + etiqueta +
                            " disponibles. Disponible: " + disponible + ", solicitado: " + solicitado);
        }
    }

    @Override
    public List<OrdenVacunacionResponseDTO> listarPorCarga(Long idCarga) {
        return ordenRepo.findByIdCargaOrderByFechaVacunacionAsc(idCarga).stream()
                .map(this::construirResponse)
                .toList();
    }

    @Override
    public DetalleVacunacionInfoDTO obtenerDetallePorId(Long idDetalleVacunacion) {
        DetalleVacunacionClienteLote d = detalleRepo.findById(idDetalleVacunacion)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de vacunacion no encontrado: " + idDetalleVacunacion));
        return toInfoDTO(d);
    }

    @Override
    public List<DetalleVacunacionInfoDTO> listarDetallesPorCarga(Long idCarga) {
        return detalleRepo.findByOrdenVacunacion_IdCarga(idCarga).stream()
                .map(this::toInfoDTO)
                .toList();
    }

    private DetalleVacunacionInfoDTO toInfoDTO(DetalleVacunacionClienteLote d) {
        return new DetalleVacunacionInfoDTO(
                d.getIdDetalleVacunacion(), d.getOrdenVacunacion().getIdCarga(), d.getCliente().getIdCliente(),
                d.getMachos1raVacunados(), d.getMachos2daVacunados(),
                d.getHembras1raVacunadas(), d.getHembras2daVacunadas()
        );
    }

    private OrdenVacunacionResponseDTO construirResponse(OrdenVacunacion orden) {
        List<DetalleVacunacionClienteLoteResponseDTO> detallesDTO = orden.getDetalles().stream()
                .map(this::construirDetalleResponse)
                .toList();

        return new OrdenVacunacionResponseDTO(
                orden.getIdOrdenVacunacion(), orden.getIdCarga(), orden.getFechaVacunacion(),
                orden.getResponsableVacunacion(), detallesDTO
        );
    }

    private DetalleVacunacionClienteLoteResponseDTO construirDetalleResponse(DetalleVacunacionClienteLote d) {
        int total = d.getMachos1raVacunados() + d.getMachos2daVacunados()
                + d.getHembras1raVacunadas() + d.getHembras2daVacunadas();

        return new DetalleVacunacionClienteLoteResponseDTO(
                d.getIdDetalleVacunacion(), d.getIdDetalleNacimiento(),
                d.getCliente().getIdCliente(), d.getCliente().getRazonSocial(),
                d.getTipoVacuna().getIdTipoVacuna(), d.getTipoVacuna().getNombreVacuna(),
                d.getMachos1raVacunados(), d.getMachos2daVacunados(),
                d.getHembras1raVacunadas(), d.getHembras2daVacunadas(), total
        );
    }
}