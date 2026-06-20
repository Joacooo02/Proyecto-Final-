package com.sistema.sistema.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO;

    @JsonCreator
    public static DiaSemana fromString(String value) {
        if (value == null) return null;

        // Convierte lo que viene del frontend a MAYÚSCULAS
        // y le quita los espacios para que coincida perfectamente.
        return DiaSemana.valueOf(value.toUpperCase().trim());
    }
}
