package com.sistema.sistema.entities.areaAcademica;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PlanEstudio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeEstudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlanEstudio")
    private Long id;

    @Column(name = "idCarrera")
    private Long idCarrera;

    @Column(name = "anioInicio")
    private Integer anioInicio;
}
