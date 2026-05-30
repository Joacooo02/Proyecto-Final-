package com.sistema.sistema.entities.usuario;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Alumno")
@PrimaryKeyJoinColumn(name = "idPersona")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Alumno extends Persona{

    @Column(nullable = false,unique = true)
    private Long legajo;

    private Integer anioIngreso;

    private boolean analiticoParcial;

    private boolean esRegular;

    private Integer planEstudio;

    private double promedio;

}
