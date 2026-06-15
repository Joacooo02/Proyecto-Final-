package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import com.sistema.sistema.enums.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota,Long> {
    List<Cuota> findByAlumnoIdPersona(Long idPersona);
    List<Cuota> findByEstadoCuota(EstadoCuota estadoCuota);
    boolean existsByAlumnoIdPersonaAndMesAndAnio(Long idPersona, Integer mes, Integer anio);
    List<Cuota> findByAlumno_IdPersonaAndEstadoCuotaIn(Long idAlumno, List<EstadoCuota> estadoCuotas);
}
