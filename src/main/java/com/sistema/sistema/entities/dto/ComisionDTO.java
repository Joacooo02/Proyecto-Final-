package com.sistema.sistema.entities.dto;

import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Profesor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComisionDTO {
    private Materia materia;
    private Profesor profesor;
    private Integer nroComision;
    private Integer cantAlumnos;
    private String aula;
}
