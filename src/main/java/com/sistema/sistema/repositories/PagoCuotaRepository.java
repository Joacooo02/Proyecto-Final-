package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.PagoCuota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoCuotaRepository extends JpaRepository<PagoCuota, Long> {
    List<PagoCuota> findByCuota_Alumno_IdPersona(Long idAlumno);
}
