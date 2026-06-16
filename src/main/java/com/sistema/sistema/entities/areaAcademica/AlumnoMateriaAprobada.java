package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Materia_Aprobada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoMateriaAprobada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Alumno alumno;

    @ManyToOne(optional = false)
    private Materia materia;

    private LocalDate fechaAprobacion;



}
