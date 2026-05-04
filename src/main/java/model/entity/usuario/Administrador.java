package model.entity.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Administrador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrador extends Persona {
    private int idAdministrador;
}
