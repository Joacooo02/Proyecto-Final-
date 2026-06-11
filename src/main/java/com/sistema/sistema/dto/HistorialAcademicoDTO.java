package com.sistema.sistema.dto;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialAcademicoDTO {
    private String materia;
    private String tipoExamen;
    private Integer nota;
    private LocalDate fechaExamen;
    private String estadoMateria;

}
