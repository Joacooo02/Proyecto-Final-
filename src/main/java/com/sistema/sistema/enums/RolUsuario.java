package com.sistema.sistema.enums;

public enum RolUsuario {
    ALUMNO,
    PROFESOR,
    ADMIN
    ///  @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'ADMIN')")
    ///  @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    ///  @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    ///  @PreAuthorize("hasAnyRole('ADMIN')")
    ///  @PreAuthorize("hasAnyRole('PROFESOR')")
    ///  @PreAuthorize("hasAnyRole('ALUMNO')")
}
