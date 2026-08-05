package com.mercohuevos.plantaincubacion.nacimiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clasificacion_pollitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionPollitos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion")
    private Long idClasificacion;

    @OneToOne
    @JoinColumn(name = "id_detalle_nacimiento", nullable = false, unique = true)
    private DetalleNacimientoLote detalleNacimiento;

    @Column(name = "machos_primera", nullable = false)
    private Integer machosPrimera;

    @Column(name = "machos_segunda", nullable = false)
    private Integer machosSegunda;

    @Column(name = "hembras_primera", nullable = false)
    private Integer hembrasPrimera;

    @Column(name = "hembras_segunda", nullable = false)
    private Integer hembrasSegunda;

    @Column(name = "pollitos_descarte", nullable = false)
    private Integer pollitosDescarte;
}