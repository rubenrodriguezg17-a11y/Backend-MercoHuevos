package com.mercohuevos.plantaincubacion.vacunacion.model;

import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_vacunacion_cliente_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVacunacionClienteLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_vacunacion")
    private Long idDetalleVacunacion;

    @ManyToOne
    @JoinColumn(name = "id_orden_vacunacion", nullable = false)
    private OrdenVacunacion ordenVacunacion;

    @Column(name = "id_detalle_nacimiento", nullable = false)
    private Long idDetalleNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_tipo_vacuna", nullable = false)
    private TipoVacuna tipoVacuna;

    @Column(name = "machos_1ra_vacunados", nullable = false)
    private Integer machos1raVacunados;

    @Column(name = "machos_2da_vacunados", nullable = false)
    private Integer machos2daVacunados;

    @Column(name = "hembras_1ra_vacunadas", nullable = false)
    private Integer hembras1raVacunadas;

    @Column(name = "hembras_2da_vacunadas", nullable = false)
    private Integer hembras2daVacunadas;
}