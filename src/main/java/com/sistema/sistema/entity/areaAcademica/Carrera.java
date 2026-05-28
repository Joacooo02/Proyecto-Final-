package com.sistema.sistema.entity.areaAcademica;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Carrera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nombre;
    private Integer duracion;
    private String tituloOtorgado;
    @Column(length = 1000)
    private Integer planEstudio;
}
