package com.sistema.sistema.services;

import com.sistema.sistema.Exceptions.CorrelativaException;
import com.sistema.sistema.Exceptions.ExamenFinalException;
import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.enums.EstadoMateria;
import com.sistema.sistema.enums.TipoInscripcion;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
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

    @Autowired
    private PeriodoInscripcionComisionRepository periodoInscripcionComisionRepository;

    @Autowired
    private CorrelatividadRepository correlatividadRepository;

    @Autowired
    private AlumnoMateriaRepository alumnoMateriaRepository;


    public void validarCorrelativas(Long idAlumno, Materia materia, TipoInscripcion tipo)
    {
        var correlativas = correlatividadRepository.findByMateria_IdMateria(materia.getIdMateria());

        for (var c : correlativas)
        {
            boolean cumple;

            cumple = alumnoMateriaRepository.existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(idAlumno,c.getMateriaCorrelativa().getIdMateria(),EstadoMateria.APROBADA);

            if(!cumple)
            {
                throw new CorrelativaException("no cumple correlativas para" +tipo);
            }
        }
    }

    public void validarInscripcionExamenFinal(Long idAlumno, Materia materia)
    {
        validarCorrelativas(idAlumno,materia,TipoInscripcion.FINAL);

        boolean regular = alumnoMateriaRepository.existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(idAlumno,materia.getIdMateria(), EstadoMateria.REGULAR);

        if(!regular)
        {
            throw new CorrelativaException("No regularizo la materia");
        }

        boolean aprobado = alumnoMateriaRepository.existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(idAlumno,materia.getIdMateria(),EstadoMateria.APROBADA);

        if(aprobado)
        {
            throw new CorrelativaException("La materia ya esta aprobada");
        }
    }

    public AlumnoMateria inscribirMateria(Long idAlumno, Long idMateria) {

        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));

        Materia materia = materiaRepository.findById(idMateria).orElseThrow(() -> new MateriaInexistente("Materia no encontrada"));

        boolean existe = materiaRepo.existsByAlumnoIdPersonaAndMateriaIdMateria(idAlumno, idMateria);

        if (existe) {
            throw new MateriaInexistente("Ya se inscribio en esta materia");
        }

        validarCorrelativas(idAlumno,materia,TipoInscripcion.CURSADA);

        AlumnoMateria inscripcion = alumnoMateriaRepository.save(AlumnoMateria.builder()
                .alumno(alumno)
                .materia(materia)
                .estado(EstadoMateria.PENDIENTE)
                .fechaInscripcion(LocalDate.now())
                .build());

        alumnoMateriaRepository.findByAlumnoAndMateria(alumno,materia).ifPresentOrElse(am -> {}, () -> alumnoMateriaRepository.save(AlumnoMateria.builder()
                .alumno(alumno)
                .materia(materia)
                .estado(EstadoMateria.INSCRIPTO)
                .build()));


        return inscripcion;
    }



    public AlumnoInscripcionComision inscribirComision(Long idAlumno, Long idComision, Long idPeriodo) {

        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));

        boolean esRegular = alumnoMateriaRepository.existsByAlumnoAndEstado(alumno,EstadoMateria.REGULAR);

        if (!esRegular) {
            throw new ComisionInvalidaException("El alumno no es regular");
        }

        Comision comision = comisionRepository.findById(idComision).orElseThrow(() -> new ComisionInvalidaException("Comisión no encontrada"));

        boolean habilitada = periodoInscripcionComisionRepository.existsByPeriodo_IdPeriodoAndComision_IdComision(idPeriodo, idComision);

        if (!habilitada) {
            throw new ComisionInvalidaException("La comision no esta habilitada");
        }

        boolean yaInscripto = comisionRepo.existsByAlumno_IdPersonaAndComision_IdComision(idAlumno,idComision);

        if (yaInscripto)
        {
            throw new ComisionInvalidaException("Ya estas inscripto en la comision");
        }

        var correlativas = correlatividadRepository.findByMateria_IdMateria(comision.getMateria().getIdMateria());

        for (var c : correlativas)
        {
            boolean aprobada = alumnoMateriaRepository.existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(idAlumno,c.getMateriaCorrelativa().getIdMateria(),EstadoMateria.APROBADA);

            if(!aprobada)
            {
                throw new MateriaInexistente("No cumple correlativas");
            }
        }

        validarCorrelativas(idAlumno,comision.getMateria(),TipoInscripcion.CURSADA);

        if(comision.getCantAlumnos() >= comision.getCupoMaximo())
        {
            throw new ComisionInvalidaException("Cupo lleno");
        }

        comision.setCantAlumnos(comision.getCantAlumnos()+1);
        comisionRepository.save(comision);

        return comisionRepo.save(AlumnoInscripcionComision.builder()
                .alumno(alumno)
                .comision(comision)
                .fechaInscripcion(LocalDate.now())
                .build());
    }

    public AlumnoInscripcionExamenFinal inscribirExamen(Long idAlumno, Long idExamen) {

        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));

        Examen examen = examenRepository.findById(idExamen).orElseThrow(() -> new ExamenFinalException("Examen no encontrado"));

        if (!examen.getTipoExamen().equals(TipoExamen.FINAL)) {
            throw new ExamenFinalException("Solo se puede inscribir a exámenes finales");
        }

        validarInscripcionExamenFinal(idAlumno,examen.getMateria());

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
