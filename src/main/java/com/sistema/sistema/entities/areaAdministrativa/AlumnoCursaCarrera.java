package com.sistema.sistema.entities.areaAdministrativa;

import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.entities.usuario.Alumno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Alumno_Cursa_Carrera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoCursaCarrera {

    @EmbeddedId
    private AlumnoCursaCarreraId id;

    @ManyToOne
    @MapsId("idAlumno")
    @JoinColumn(name = "idAlumno")
    private Alumno alumno;

    @ManyToOne
    @MapsId("idCarrera")
    @JoinColumn(name = "idCarrera")
    private Carrera carrera;

    private LocalDate fecha_inscripcion;
}
