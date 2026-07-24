package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registro_nacimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroNacimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nacimiento")
    private Long idNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_carga", nullable = false, unique = true)
    private Carga carga;   // 1 nacimiento por carga

    @Column(name = "cantidad_machos", nullable = false)
    private Integer cantidadMachos;

    @Column(name = "cantidad_hembras", nullable = false)
    private Integer cantidadHembras;

    @Column(name = "cantidad_primera", nullable = false)
    private Integer cantidadPrimera;

    @Column(name = "cantidad_segunda", nullable = false)
    private Integer cantidadSegunda;

    @Column(name = "cantidad_descarte", nullable = false)
    private Integer cantidadDescarte;
}