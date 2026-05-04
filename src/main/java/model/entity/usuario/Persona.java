package model.entity.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class Persona {
    private String nombre;
    private String apellido;
    private String dni;
    private String direccion;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String email;
}
