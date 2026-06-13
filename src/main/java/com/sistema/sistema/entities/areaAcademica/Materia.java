package com.sistema.sistema.entities.areaAcademica;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistema.sistema.dto.CorrelativaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    @JsonIgnore
    private Carrera carrera;

    @Column(length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "correlativas")
    private List<CorrelativaDTO> correlativas;

    private Integer cargaHoraria;
    private Integer cuatrimestre;
    private Integer anioCursado;
}
