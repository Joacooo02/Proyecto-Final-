package com.sistema.sistema.enums;

public enum EstadoMateria {
    PENDIENTE,
    CURSANDO,
    REGULAR,
    APROBADA,
    INSCRIPTO;

    public boolean cumpleCon(EstadoCorrelatividad requerido) {
        if (requerido == EstadoCorrelatividad.REGULAR) {
            return this == REGULAR || this == APROBADA;
        }
        if (requerido == EstadoCorrelatividad.APROBADA) {
            return this == APROBADA;
        }
        return true; // Si es PENDIENTE o null, pasa libremente
    }
}
