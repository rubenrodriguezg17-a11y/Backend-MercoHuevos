// despacho/model/DespachoPollitos.java
package com.mercohuevos.plantaincubacion.despacho.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.mercohuevos.plantaincubacion.shared.model.Cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "despacho_pollitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespachoPollitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho")
    private Long idDespacho;

    @Column(name = "id_carga", nullable = false)
    private Long idCarga;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_despacho", nullable = false)
    private LocalDate fechaDespacho;

    @Column(name = "hora_despacho", nullable = false)
    private LocalTime horaDespacho;

    @Column(name = "placa_vehiculo", nullable = false)
    private String placaVehiculo;

    @Column(name = "nombre_conductor", nullable = false)
    private String nombreConductor;

    @Column(name = "destino", nullable = false)
    private String destino;

    @OneToMany(mappedBy = "despacho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleDespachoLote> detalles = new ArrayList<>();
}