package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "embandejado_detalle",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_recepcion", "codigo_lote_granja"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmbandejadoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_embandejado")
    private Long idEmbandejado;

    @ManyToOne
    @JoinColumn(name = "id_recepcion", nullable = false)
    private RecepcionReporte recepcion;

    @ManyToOne
    @JoinColumn(name = "id_fusion_lote")
    private FusionLote fusionLote;

    @Column(name = "codigo_lote_granja", nullable = false)
    private String codigoLoteGranja;

    @Column(name = "huevos_incubables_guia", nullable = false)
    private Integer huevosIncubablesGuia;

    @Column(name = "huevos_comercial_guia", nullable = false)
    private Integer huevosComercialGuia;

    @Column(name = "rotos_transporte")
    private Integer rotosTransporte;

    @Column(name = "rotos_embandejado")
    private Integer rotosEmbandejado;

    @Column(name = "seleccion_descartada", nullable = false)
    private Integer seleccionDescartada;

    @Column(name = "observaciones")
    private String observaciones;
}