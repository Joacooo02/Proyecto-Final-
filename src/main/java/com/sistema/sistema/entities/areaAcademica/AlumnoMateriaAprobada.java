package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "alumno_materia_aprobada")
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
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @Column(name = "fecha_aprobacion")
    private LocalDate fechaAprobacion;



}
