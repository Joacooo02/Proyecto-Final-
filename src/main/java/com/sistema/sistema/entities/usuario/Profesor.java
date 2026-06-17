package com.sistema.sistema.entities.usuario;
import com.sistema.sistema.enums.EstadoProfesor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Profesor")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Profesor extends Persona{

    private Integer horasSemanales;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProfesor estadoProfesor;
}