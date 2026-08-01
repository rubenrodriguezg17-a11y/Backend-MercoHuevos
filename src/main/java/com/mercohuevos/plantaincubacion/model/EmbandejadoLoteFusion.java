package com.mercohuevos.plantaincubacion.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "embandejado_lote_fusion")
@Getter
@Setter
public class EmbandejadoLoteFusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmbandejadoLoteFusion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_embandejado_general", nullable = false)
    private EmbandejadoGeneral embandejadoGeneral;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @Column(name = "rotos_transporte", nullable = false)
    private Integer rotosTransporte = 0;

    @Column(name = "rotos_embandejado", nullable = false)
    private Integer rotosEmbandejado = 0;

    @Column(name = "seleccion_descartada", nullable = false)
    private Integer seleccionDescartada = 0;

    @Column
    private String observaciones;

    @OneToMany(mappedBy = "embandejadoLoteFusion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConteoCategoriaEmbandejado> conteos = new ArrayList<>();
}