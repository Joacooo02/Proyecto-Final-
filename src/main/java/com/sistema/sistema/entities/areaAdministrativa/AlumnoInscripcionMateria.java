package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Inscripcion_Materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoInscripcionMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materia materia;

    private LocalDate fecha_inscripcion;

}

