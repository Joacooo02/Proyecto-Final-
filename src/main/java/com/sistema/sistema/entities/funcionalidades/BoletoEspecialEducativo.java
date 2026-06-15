package com.sistema.sistema.entities.funcionalidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletoEspecialEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "")
    private Long alumnoId;

    @Column(name = "")
    private Boolean fueSolicitado;

    @Column(name = "")
    private Boolean estaActivo;
}
