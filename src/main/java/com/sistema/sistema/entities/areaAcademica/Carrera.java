package com.sistema.sistema.entities.areaAcademica;

import com.sistema.sistema.entities.enums.ModalidadCarrera;
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
    private Long idCarrera;

    @Column(length = 50)
    private String nombre;

    private Integer duracion;

    @Column(length = 50)
    private String tituloOtorgado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadCarrera modalidadCarrera;

    private Integer planDeEstudio;
}
