package com.mercohuevos.plantaincubacion.nacimiento.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nacimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nacimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nacimiento")
    private Long idNacimiento;

    @Column(name = "id_carga", nullable = false, unique = true)
    private Long idCarga;   // 1 nacimiento por carga

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "responsable", nullable = false)
    private String responsable;

    @OneToMany(mappedBy = "nacimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleNacimientoLote> detalles = new ArrayList<>();
}