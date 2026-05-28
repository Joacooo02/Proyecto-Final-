package com.sistema.sistema.entity.areaAcademica;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Comision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idComision;
    private Integer numeroComision;
    private String aula;
}
