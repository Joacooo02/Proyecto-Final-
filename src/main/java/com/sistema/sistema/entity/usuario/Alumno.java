package com.sistema.sistema.entity.usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Year;

@Entity
@Table(name = "Alumno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumno extends Persona {
    private int idAlumno;
    private int legajo;
    private Year anioIngreso;
    private boolean analiticoParcial;
    private boolean esRegular;
    private Year planEstudio;
    private double promedio;
}
