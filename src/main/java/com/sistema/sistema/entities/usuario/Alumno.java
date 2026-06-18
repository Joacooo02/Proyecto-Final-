package com.sistema.sistema.entities.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    private double promedio;

    @JsonIgnore
    @OneToMany(mappedBy = "alumno")
    private List<AlumnoInscripcionComision> inscripcionComision = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "alumno")
    private List<AlumnoMateria> inscripcionesMateria = new ArrayList<>();

}