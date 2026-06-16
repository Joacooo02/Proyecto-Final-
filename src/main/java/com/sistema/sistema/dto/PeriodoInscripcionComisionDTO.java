package com.sistema.sistema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodoInscripcionComisionDTO {

    private Long id;
    private Long idPeriodo;
    private Long idComision;
    private String materia;
    private Integer nroComision;

}
