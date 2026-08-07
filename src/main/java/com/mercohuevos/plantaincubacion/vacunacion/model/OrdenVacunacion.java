package com.mercohuevos.plantaincubacion.vacunacion.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orden_vacunacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenVacunacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_vacunacion")
    private Long idOrdenVacunacion;

    @Column(name = "id_carga", nullable = false)
    private Long idCarga;

    @Column(name = "fecha_vacunacion", nullable = false)
    private LocalDateTime fechaVacunacion;

    @Column(name = "responsable_vacunacion", nullable = false)
    private String responsableVacunacion;

    @OneToMany(mappedBy = "ordenVacunacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVacunacionClienteLote> detalles = new ArrayList<>();
}