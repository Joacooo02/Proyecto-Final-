package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Inscripcion_Comision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoInscripcionComision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "idComision", nullable = false)
    private Comision comision;

    private LocalDate fechaInscripcion;

}
