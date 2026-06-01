package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia,Long> {
    List<Materia> findByCarreraIdCarrera(Long idCarrera);
}
