package com.sistema.sistema.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NotaDTO {
    private Long id;
    private Long idExamn;
    private Long idAlumno;
    private Integer nota;
    private LocalDate fechaRegistro;
}
