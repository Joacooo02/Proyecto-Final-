package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota,Long> {
}
