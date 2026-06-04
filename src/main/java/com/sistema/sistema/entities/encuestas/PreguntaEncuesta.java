package com.sistema.sistema.entities.encuestas;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PreguntaEncuesta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PreguntaEncuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreguntaEncuesta;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false, length = 300)
    private String enunciado;
}

