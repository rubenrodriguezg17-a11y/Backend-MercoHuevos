package com.mercohuevos.plantaincubacion.recepcion.model;

import com.mercohuevos.plantaincubacion.shared.model.FusionLotePlantilla;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fusion_lote_plantilla_detalle")
@Getter
@Setter
public class FusionLotePlantillaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private FusionLotePlantilla plantilla;

    @Column(name = "id_lote_granja", nullable = false)
    private Long idLoteGranja;

    @Column(name = "codigo_lote_granja", nullable = false)
    private String codigoLoteGranja;
}