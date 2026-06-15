package com.sistema.sistema.entities.funcionalidades;

import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boleto_especial_educativo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletoEspecialEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idAlumno", nullable = false)
    private Alumno alumno;

    @Column(name = "fueSolicitado")
    private Boolean fueSolicitado;

    @Column(name = "estaActivo")
    private Boolean estaActivo;
}
