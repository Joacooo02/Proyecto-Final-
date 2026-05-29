package com.sistema.sistema.entities.usuario;
import com.sistema.sistema.entities.enums.EstadoProfesor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Profesor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProfesor;

    @OneToOne
    @JoinColumn(name = "idPersona", nullable = false)
    private Persona persona;

    private Integer horasSemanales;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProfesor estadoProfesor;
}