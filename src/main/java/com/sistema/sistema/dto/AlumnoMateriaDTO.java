package com.sistema.sistema.dto;

import com.sistema.sistema.enums.EstadoMateria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoMateriaDTO {
    private Long id;
    private Long idAlumno;
    private String alumnoNombre;
    private Long idMateria;
    private String materiaNombre;
    private EstadoMateria estado;
    private Double notaFinal;
    private LocalDate fechaAprobacion;
}