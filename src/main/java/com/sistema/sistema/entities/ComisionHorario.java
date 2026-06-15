package com.sistema.sistema.entities;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/*
CREATE TABLE ComisionHorario(
    idComisionHorario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idComision BIGINT UNSIGNED NOT NULL,

    diaSemana ENUM(
        'LUNES',
        'MARTES',
        'MIERCOLES',
        'JUEVES',
        'VIERNES',
        'SABADO'
    ) NOT NULL,

    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,

    FOREIGN KEY (idComision)
        REFERENCES Comision(idComision)
        ON DELETE CASCADE
);
*/

@Entity
@Table(name = "ComisionHorario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ComisionHorario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "idComisionHorario")
    private Long id;

    @Column(name = "idComision")
    private Long idComision;

    @Enumerated(EnumType.STRING)
    @Column(name = "diaSemana", nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "horaInicio",nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFin",nullable = false)
    private LocalTime horaFin;
}
