package com.sistema.sistema.Security.Controller;

import com.sistema.sistema.enums.RolUsuario;
import lombok.Getter;

/// Datos para crear un usuario
public record RegisterRequest (
        String email,
        String nombre,
        String contrasena,
        RolUsuario role
){}
