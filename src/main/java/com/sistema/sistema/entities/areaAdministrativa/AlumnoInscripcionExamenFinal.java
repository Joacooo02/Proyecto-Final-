package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Inscripcion_Examen_Final")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoInscripcionExamenFinal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "idExamen", nullable = false)
    private Examen examen;

    @Column(name = "fechaInscripcion")
    private LocalDate fechaInscripcion;
}
