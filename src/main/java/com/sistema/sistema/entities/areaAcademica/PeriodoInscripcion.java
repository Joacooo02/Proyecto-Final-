package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.enums.TipoInscripcion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PeriodoInscripcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoInscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPeriodo")
    private Long idPeriodo;

    @Column(name = "idCarrera")
    private Long idCarrera;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoInscripcion tipoInscripcion;

    @Column(name = "anioLectivo")
    private Integer anioLectivo;

    @Column(name = "cuatrimestre")
    private Integer cuatrimestre;

    @Column(name = "fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fechaCierre")
    private LocalDateTime fechaCierre;

    @Column(name = "activa")
    private Boolean activa;
}
