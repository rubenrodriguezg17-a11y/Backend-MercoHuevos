package com.mercohuevos.granja.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.mercohuevos.granja.enums.EstadoReporte;
import com.mercohuevos.granja.model.ReporteTraslado;

public class ReporteTrasladoSpecifications {

    private ReporteTrasladoSpecifications() {}

    public static Specification<ReporteTraslado> conEstado(EstadoReporte estado) {
        return (root, query, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
    }

    public static Specification<ReporteTraslado> conFechaEntre(LocalDate inicio, LocalDate fin) {
        return (root, query, cb) -> {
            if (inicio != null && fin != null) return cb.between(root.get("fecha"), inicio, fin);
            if (inicio != null) return cb.greaterThanOrEqualTo(root.get("fecha"), inicio);
            if (fin != null) return cb.lessThanOrEqualTo(root.get("fecha"), fin);
            return null;
        };
    }

    public static Specification<ReporteTraslado> conChoferLike(String chofer) {
        return (root, query, cb) -> (chofer == null || chofer.isBlank())
                ? null
                : cb.like(cb.lower(root.get("chofer")), "%" + chofer.toLowerCase() + "%");
    }

    public static Specification<ReporteTraslado> construir(EstadoReporte estado, LocalDate fechaInicio,
                                                           LocalDate fechaFin, String chofer) {
        return Specification.where(conEstado(estado))
                .and(conFechaEntre(fechaInicio, fechaFin))
                .and(conChoferLike(chofer));
    }
}