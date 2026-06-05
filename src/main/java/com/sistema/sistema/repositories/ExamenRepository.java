package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.enums.TipoExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Long> {
    List<Examen> findByTipoExamen(TipoExamen tipoExamen);
    List<Examen> findByMateriaNombre(String nombreMateria);
    List<Examen> findByFecha(LocalDate fechaExamen);

}
