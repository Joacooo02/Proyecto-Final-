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
    private Long idComision;
    private Integer nroComision;
    private String aula;
    private Integer cantAlumnos;
    private String materiaNombre;
}
