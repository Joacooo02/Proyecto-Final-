package com.sistema.sistema.entities.areaAdministrativa;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AlumnoCursaCarreraId implements Serializable {
    private Long idAlumno;
    private Long idCarrera;
}
