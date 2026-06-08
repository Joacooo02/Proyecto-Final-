package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.entities.enums.TipoExamen;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.repositories.*;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private InscripcionExamenFinalRepository alumnoExamenRepo;

    @Autowired
    private ExamenRepository examenRepository;

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

        if(comisionRepo.existsByAlumnoIdPersonaAndComisionMateriaIdMateria(idAlumno, comision.getMateria().getIdMateria()))
        {
            throw new RuntimeException("El alumno ya esta inscripto en una comision de esta materiq");
        }

        if(comision.getCantAlumnos() >= 50)
        {
            throw new RuntimeException("La comision esta completa");
        }

        AlumnoInscripcionComision inscripcion = AlumnoInscripcionComision.builder()
                .alumno(alumno)
                .comision(comision)
                .build();

        comision.setCantAlumnos(comision.getCantAlumnos() + 1);
        comisionRepository.save(comision);


        return comisionRepo.save(inscripcion);
    }

    public AlumnoInscripcionExamenFinal inscribirExamen(Long idAlumno, Long idExamen) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Examen examen = examenRepository.findById(idExamen)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));

        if (!examen.getTipoExamen().equals(TipoExamen.FINAL)) {
            throw new RuntimeException("Solo se puede inscribir a exámenes finales");
        }

        AlumnoInscripcionExamenFinal inscripcion = AlumnoInscripcionExamenFinal.builder()
                .alumno(alumno)
                .examen(examen)
                .fecha_inscripcion(LocalDate.now())
                .build();

        return alumnoExamenRepo.save(inscripcion);
    }


    public List<Comision> obtenerComisionesDisponibles(Long idMateria)
    {
        List<Comision> comisiones = comisionRepository.findByMateria_IdMateria(idMateria);

        List<Comision> disponibles = new ArrayList<>();

        for (Comision comision : comisiones)
        {
            if(comision.getCantAlumnos() < 50)
            {
                disponibles.add(comision);
            }
        }
        return disponibles;
    }


}
