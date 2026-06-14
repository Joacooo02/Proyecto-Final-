package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Correlatividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorrelatividadRepository extends JpaRepository<Correlatividad, Long> {
    List<Correlatividad> findByMateriaIdMateria(Long idMateria);
}
