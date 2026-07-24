package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lote_origen_reporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoteOrigenReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote_origen")
    private Long idLoteOrigen;

    @ManyToOne
    @JoinColumn(name = "id_recepcion", nullable = false)
    private RecepcionReporte recepcion;

    @Column(name = "codigo_lote_granja", nullable = false)
    private String codigoLoteGranja;

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "huevos_incubables_guia", nullable = false)
    private Integer huevosIncubablesGuia;   // suma A+B+C tal cual vino del evento

    @Column(name = "huevos_comercial_guia", nullable = false)
    private Integer huevosComercialGuia;    // suma COMERCIAL+DY tal cual vino del evento
}