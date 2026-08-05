package com.mercohuevos.plantaincubacion.transferencia.model;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_transferencia_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleTransferenciaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_transferencia")
    private Long idDetalleTransferencia;

    @ManyToOne
    @JoinColumn(name = "id_transferencia", nullable = false)
    private Transferencia transferencia;

    @Column(name = "id_carga_lote", nullable = false)
    private Long idCargaLote;

    @ManyToOne
    @JoinColumn(name = "id_maquina_origen", nullable = false)
    private Maquina maquinaOrigen;

    @ManyToOne
    @JoinColumn(name = "id_nacedora_destino", nullable = false)
    private Maquina nacedoraDestino;

    @Column(name = "huevos_transferidos", nullable = false)
    private Integer huevosTransferidos;

    @Column(name = "liberado", nullable = false)
    private Boolean liberado = false;
}