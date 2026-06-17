package com.sistema.sistema.Security.Controller;

/// Datos para crear un usuario
public record RegisterRequest (
        String email,
        String nombre,
        String contraseña
){}
