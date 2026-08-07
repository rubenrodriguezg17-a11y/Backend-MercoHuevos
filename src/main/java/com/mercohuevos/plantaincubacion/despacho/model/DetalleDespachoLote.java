// despacho/model/DetalleDespachoLote.java
package com.mercohuevos.plantaincubacion.despacho.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_despacho_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDespachoLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_despacho")
    private Long idDetalleDespacho;

    @ManyToOne
    @JoinColumn(name = "id_despacho", nullable = false)
    private DespachoPollitos despacho;

    @Column(name = "id_detalle_vacunacion", nullable = false)
    private Long idDetalleVacunacion;

    @Column(name = "machos_1ra_despachados", nullable = false)
    private Integer machos1raDespachados;

    @Column(name = "machos_2da_despachados", nullable = false)
    private Integer machos2daDespachados;

    @Column(name = "hembras_1ra_despachadas", nullable = false)
    private Integer hembras1raDespachadas;

    @Column(name = "hembras_2da_despachadas", nullable = false)
    private Integer hembras2daDespachadas;
}