package com.sistema.sistema.services;

import com.sistema.sistema.exceptions.InscripcionInvalida;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.enums.TipoExamen;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.ComisionInvalidaException;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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

        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Materia materia = materiaRepository.findById(idMateria).orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        boolean existe = materiaRepo.existsByAlumnoIdPersonaAndMateriaIdMateria(idAlumno, idMateria);

        if (existe) {
            throw new MateriaInexistente("Ya se inscribio en esta materia");
        }

        return materiaRepo.save(AlumnoInscripcionMateria.builder()
                .alumno(alumno)
                .materia(materia)
                .fechaInscripcion(LocalDate.now())
                .build());
    }


    public AlumnoInscripcionComision inscribirComision(Long idAlumno, Long idComision) {

        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));

        Comision comision = comisionRepository.findById(idComision).orElseThrow(() -> new ComisionInvalidaException("Comisión no encontrada"));

        boolean estaInscripto = materiaRepo.existsByAlumnoIdPersonaAndMateriaIdMateria(idAlumno, comision.getMateria().getIdMateria());

        if (!estaInscripto)
        {
            throw new MateriaInexistente("Tenes que inscribirte a la materia");
        }
        boolean yaInscriptoComision = comisionRepo.existsByAlumnoIdPersonaAndComisionIdComision(idAlumno, idComision);

        if (yaInscriptoComision)
        {
            throw new InscripcionInvalida("Ya estás inscripto en una comisión de esta materia");
        }

        if (comision.getCantAlumnos() >= 50)
        {
            throw new ComisionInvalidaException("La comisión está completa");
        }


        comision.setCantAlumnos(comision.getCantAlumnos() + 1);
        comisionRepository.save(comision);


        return comisionRepo.save(
                AlumnoInscripcionComision.builder()
                        .alumno(alumno)
                        .comision(comision)
                        .fechaInscripcion(LocalDate.now())
                        .build());
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
                .fechaInscripcion(LocalDate.now())
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
