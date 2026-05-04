package model.entity.areaAcademica;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Examen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idExamen;
    private LocalDateTime fecha;

}
