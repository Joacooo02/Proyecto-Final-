package com.sistema.sistema.dto;

import com.sistema.sistema.enums.TipoInscripcion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoInscripcionDTO {
    private Long id;
    private Long idCarrera;
    private TipoInscripcion tipoInscripcion;
    private Integer anioLectivo;
    private Integer cuatrimestre;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private Boolean activa;
}
