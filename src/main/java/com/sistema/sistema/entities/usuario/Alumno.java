package com.sistema.sistema.entities.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Alumno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlumno;

    @Column(nullable = false,unique = true)
    private Integer legajo;

    private Integer anioIngreso;

    private boolean analiticoParcial;

    private boolean esRegular;

    private Integer planEstudio;

    private double promedio;

    @OneToOne
    @JoinColumn(name = "idPersona", nullable = false)
    private Persona persona;

}
