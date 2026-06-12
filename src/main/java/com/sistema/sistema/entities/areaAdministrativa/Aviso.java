package com.sistema.sistema.entities.areaAdministrativa;
import com.sistema.sistema.entities.usuario.Persona;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aviso {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(name = "fecha_aviso", nullable = false)
    private LocalDateTime fechaAviso;




}
