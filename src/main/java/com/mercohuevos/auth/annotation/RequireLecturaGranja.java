package com.mercohuevos.auth.annotation;

import java.lang.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Permite el acceso a ADMIN (lee todo) o a cualquier usuario cuya area
 * asignada sea GRANJA (encargado de granja, logistica de granja, empleados de granja, etc).
 * Pensada para metodos de SOLO LECTURA (GET).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or hasAuthority('AREA_GRANJA')")
public @interface RequireLecturaGranja {}