package com.mercohuevos.auth.model;

import com.mercohuevos.auth.enums.Area;
import com.mercohuevos.auth.enums.Rol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "dni"))
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "dni", nullable = false, length = 15)
    private String dni;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "password")
    private String password;

    @Column(name = "password_configurada")
    private boolean passwordConfigurada = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private Area area;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos = 0;

    @Column(name = "bloqueado", nullable = false)
    private boolean bloqueado = false;
}