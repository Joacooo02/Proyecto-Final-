package com.sistema.sistema.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaAviso;

}
