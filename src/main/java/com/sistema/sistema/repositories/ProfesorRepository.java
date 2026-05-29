package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.usuario.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor,Integer> {
}
