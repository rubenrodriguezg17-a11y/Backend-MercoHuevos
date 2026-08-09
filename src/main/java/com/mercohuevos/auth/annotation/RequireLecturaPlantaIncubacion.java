package com.mercohuevos.auth.annotation;

import java.lang.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Permite el acceso a ADMIN (lee todo) o a cualquier usuario cuya area
 * asignada sea PLANTA_INCUBACION (encargado de planta, logistica, embandejado,
 * vacunacion, nacimiento, empleados de planta, etc).
 * Pensada para metodos de SOLO LECTURA (GET).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or hasAuthority('AREA_PLANTA_INCUBACION')")
public @interface RequireLecturaPlantaIncubacion {}