package com.sistema.sistema.entities.dto;

import com.sistema.sistema.entities.areaAcademica.Carrera;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO {
    private Carrera carrera;
    private String nombre;
    private Integer cargaHoraria;
    private Integer cuatrimestre;
    private Integer anioCursado;
}