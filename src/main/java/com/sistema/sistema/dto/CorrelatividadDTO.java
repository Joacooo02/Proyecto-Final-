package com.sistema.sistema.dto;

import com.sistema.sistema.enums.EstadoCorrelatividad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CorrelatividadDTO {
    private Long idCorrelatividad;
    private Long idMateria;
    private Long idMateriaCorrelativa;
    private EstadoCorrelatividad estadoParaCursar;
    private EstadoCorrelatividad estadoParaRendir;
}
