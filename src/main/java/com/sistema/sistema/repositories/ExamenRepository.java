package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {
}
