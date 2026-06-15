package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.funcionalidades.BoletoEspecialEducativo;
import com.sistema.sistema.entities.usuario.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoEspecialEducativoRepository extends JpaRepository<BoletoEspecialEducativo, Long> {
    boolean existsByAlumno(Alumno alumno);

}
