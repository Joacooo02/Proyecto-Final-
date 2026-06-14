package com.sistema.sistema.entities.areaAcademica;
import com.sistema.sistema.enums.EstadoCorrelatividad;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Correlatividad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Correlatividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCorrelatividad;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "idMateriaCorrelativa", nullable = false)
    private Materia materiaCorrelativa;

    @Enumerated(EnumType.STRING)
    private EstadoCorrelatividad estadoParaCursar;

    @Enumerated(EnumType.STRING)
    private EstadoCorrelatividad estadoParaRendir;
}
