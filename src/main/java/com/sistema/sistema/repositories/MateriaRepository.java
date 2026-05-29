package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia,Integer> {
}
