package com.mercohuevos.auth.annotation;

import java.lang.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('LOGISTICA_INCUBACION')")
public @interface RequireLogistica {}