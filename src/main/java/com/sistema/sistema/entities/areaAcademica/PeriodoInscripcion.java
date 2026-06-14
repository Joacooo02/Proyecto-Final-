package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.enums.TipoInscripcion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "PeriodoInscripcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoInscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idCarrera")
    private Long idCarrera;

    private TipoInscripcion tipoInscripcion;

    @Column(name = "anioLectivo")
    private Integer anioLectivo;

    @Column(name = "cuatrimestre")
    private Integer cuatrimestre;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "fechaCierre")
    private LocalDate fechaCierre;

    @Column(name = "activa")
    private Boolean activa;
}
