package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.usuario.Profesor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Comision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Comision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComision;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "idProfesor")
    private Profesor profesor;

    private Integer nroComision;
    private Integer cantAlumnos = 0;

    @Column(length = 50)
    private String aula;

    private Integer cupoMaximo = 50;
}
