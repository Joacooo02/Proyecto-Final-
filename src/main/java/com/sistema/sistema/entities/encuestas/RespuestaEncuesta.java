package com.sistema.sistema.entities.encuestas;

import com.sistema.sistema.entities.areaAcademica.Comision;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "RespuestaEncuesta", uniqueConstraints = @UniqueConstraint(columnNames = {"idComision", "hashAlumno"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RespuestaEncuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuestaEncuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComision", nullable = false)
    private Comision comision;

    @Column(nullable = false, length = 64)
    private String hashAlumno;

    @Column(nullable = false)
    private LocalDateTime fechaRespuesta;

    @Column(length = 1000)
    private String comentarioFinal;

    @OneToMany(mappedBy = "respuestaEncuesta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaPregunta> respuestas;

    //Metodo para generar fecha al momento del insert
    @PrePersist
    public void prePersist() {
        this.fechaRespuesta = LocalDateTime.now();
    }
}
