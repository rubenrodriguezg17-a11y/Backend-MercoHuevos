package com.mercohuevos.plantaincubacion.recepcion.model;

import java.time.LocalDate;

import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recepcion_reporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Long idRecepcion;

    @Column(name = "id_reporte_granja", nullable = false, unique = true)
    private Long idReporteGranja;

    @Column(name = "numero_reporte_granja", nullable = false)
    private String numeroReporteGranja;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDate fechaReporte;

    @Column(name = "embandejado_confirmado", nullable = false)
    private boolean embandejadoConfirmado = false;

    @Column(name = "conteo_comercial_confirmado", nullable = false)
    private boolean conteoComercialConfirmado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoRecepcion estado = EstadoRecepcion.PENDIENTE;
}