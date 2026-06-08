package com.sistema.sistema.entities.usuario;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
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

    private Integer planEstudio;

    private double promedio;

    @OneToMany(mappedBy = "alumno")
    private List<AlumnoInscripcionMateria> inscripcionMateriaList;

}
