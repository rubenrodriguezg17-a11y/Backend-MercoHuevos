package com.mercohuevos.plantaincubacion.recepcion.model;

import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fusion_lote_detalle")
@Getter
@Setter
public class FusionLoteDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lote_origen_reporte", nullable = false, unique = true)
    private LoteOrigenReporte loteOrigenReporte;
}