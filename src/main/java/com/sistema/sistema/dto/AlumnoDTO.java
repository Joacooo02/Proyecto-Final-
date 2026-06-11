package com.sistema.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoDTO {

    private Long legajo;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String email;
    private Integer anioIngreso;
    private boolean analiticoParcial;
    private boolean esRegular;
    private Integer planEstudio;
    private double promedio;
}
