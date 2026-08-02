package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fusion_lote", uniqueConstraints =
    @UniqueConstraint(name = "uk_fusion_recepcion_linea_codigo",
        columnNames = {"id_recepcion", "id_linea_genetica", "codigo_fusion"}))
@Getter
@Setter
public class FusionLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFusionLote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_recepcion", nullable = false)
    private RecepcionReporte recepcion;

    @Column(name = "id_linea_genetica", nullable = false)
    private Long idLineaGenetica;

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "codigo_fusion", nullable = false)
    private String codigoFusion;

    @Column(name = "huevos_incubables_guia", nullable = false)
    private Integer huevosIncubablesGuia;

    @Column(name = "huevos_comercial_guia", nullable = false)
    private Integer huevosComercialGuia;

    @Column(nullable = false)
    private Boolean activa = true;
}