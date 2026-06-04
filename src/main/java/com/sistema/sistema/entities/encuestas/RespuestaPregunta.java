package com.sistema.sistema.entities.encuestas;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RespuestaPregunta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RespuestaPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuestaPregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRespuestaEncuesta", nullable = false)
    private RespuestaEncuesta respuestaEncuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPreguntaEncuesta", nullable = false)
    private PreguntaEncuesta preguntaEncuesta;

    @Column(nullable = false)
    private Integer calificacion;

}

