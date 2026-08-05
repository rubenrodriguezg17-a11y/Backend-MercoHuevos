package com.mercohuevos.plantaincubacion.nacimiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_nacimiento_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleNacimientoLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_nacimiento")
    private Long idDetalleNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_nacimiento", nullable = false)
    private Nacimiento nacimiento;

    @Column(name = "id_detalle_transferencia", nullable = false)
    private Long idDetalleTransferencia;

    @Column(name = "id_carga_lote", nullable = false)
    private Long idCargaLote;

    @Column(name = "huevos_transferidos", nullable = false)
    private Integer huevosTransferidos;

    @Column(name = "no_nacidos", nullable = false)
    private Integer noNacidos;

    @OneToOne(mappedBy = "detalleNacimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClasificacionPollitos clasificacion;
}