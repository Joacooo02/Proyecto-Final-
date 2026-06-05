package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InscripcionService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private ComisionRepository comisionRepository;

    @Autowired
    private AlumnoInscripcionMateriaRepository materiaRepo;

    @Autowired
    private AlumnoInscripcionComisionRepository comisionRepo;

    public AlumnoInscripcionMateria inscribirMateria(Long idAlumno, Long idMateria) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Materia materia = materiaRepository.findById(idMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        AlumnoInscripcionMateria inscripcion = AlumnoInscripcionMateria.builder()
                .alumno(alumno)
                .materia(materia)
                .fecha_inscripcion(LocalDate.now())
                .build();

        return materiaRepo.save(inscripcion);
    }

    public AlumnoInscripcionComision inscribirComision(Long idAlumno, Long idComision) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Comision comision = comisionRepository.findById(idComision)
                .orElseThrow(() -> new RuntimeException("Comisión no encontrada"));

        AlumnoInscripcionComision inscripcion = AlumnoInscripcionComision.builder()
                .alumno(alumno)
                .comision(comision)
                .build();

        return comisionRepo.save(inscripcion);
    }
}
