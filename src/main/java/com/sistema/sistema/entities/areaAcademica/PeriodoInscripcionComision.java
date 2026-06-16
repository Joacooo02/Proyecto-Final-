package com.sistema.sistema.entities.areaAcademica;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "periodo_inscripcion_comision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodoInscripcionComision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idPeriodo")
    private PeriodoInscripcion periodo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idComision", nullable = false)
    private Comision comision;
}
