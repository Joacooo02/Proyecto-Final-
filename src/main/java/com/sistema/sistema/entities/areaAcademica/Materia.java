package com.sistema.sistema.entities.areaAcademica;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMateria;

    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    private Carrera carrera;

    @Column(length = 50)
    private String nombre;

    private Integer cargaHoraria;
    private Integer cuatrimestre;
    private Integer anioCursado;
}
