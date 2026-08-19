package com.mercohuevos.plantaincubacion.recepcion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conteo_comercial_linea")
@Getter
@Setter
public class ConteoComercialLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conteo_comercial")
    private Long idConteoComercial;

    @ManyToOne
    @JoinColumn(name = "id_recepcion", nullable = false)
    private RecepcionReporte recepcion;

    @Column(name = "id_linea_genetica", nullable = false)
    private Long idLineaGenetica;

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "cantidad_guia", nullable = false)
    private Integer cantidadGuia;

    @Column(name = "cantidad_contada", nullable = false)
    private Integer cantidadContada;

    @Column(name = "diferencia", nullable = false)
    private Integer diferencia;

    @Column(name = "conforme", nullable = false)
    private boolean conforme;

    @Column(name = "fecha_conteo", nullable = false)
    private LocalDateTime fechaConteo = LocalDateTime.now();
}