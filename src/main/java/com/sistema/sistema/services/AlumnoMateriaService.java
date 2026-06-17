package com.sistema.sistema.services;

import com.sistema.sistema.dto.AlumnoMateriaDTO;
import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoMateria;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.mappers.AlumnoMateriaMapper;
import com.sistema.sistema.repositories.AlumnoMateriaRepository;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoMateriaService {

    private final AlumnoMateriaRepository alumnoMateriaRepository;
    private final AlumnoRepository alumnoRepository;
    private final MateriaRepository materiaRepository;
    private final AlumnoMateriaMapper alumnoMateriaMapper;

    public AlumnoMateria buscarPorId(Long id) {
        return alumnoMateriaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Alumno_Materia con id: " + id + " no encontrado"));
    }

    public List<AlumnoMateriaDTO> listar() {
        return alumnoMateriaMapper.toDTOList(alumnoMateriaRepository.findAll());
    }

    public AlumnoMateriaDTO obtenerPorId(Long id) {
        return alumnoMateriaMapper.toDTO(buscarPorId(id));
    }

    public List<AlumnoMateriaDTO> historialAcademico(Long idAlumno) {
        buscarAlumno(idAlumno);
        return alumnoMateriaMapper.toDTOList(alumnoMateriaRepository.findByAlumno_IdPersona(idAlumno));
    }

    public AlumnoMateriaDTO crear(AlumnoMateriaDTO dto) {

        Alumno alumno = buscarAlumno(dto.getIdAlumno());
        Materia materia = buscarMateria(dto.getIdMateria());

        boolean existe = alumnoMateriaRepository.existsByAlumnoAndMateria(alumno, materia);

        if (existe)
        {
            throw new AlumnoInvalidoException("El alumno ya se inscribio en la materia");
        }

        AlumnoMateria alumnoMateria = new AlumnoMateria();
        alumnoMateria.setAlumno(alumno);
        alumnoMateria.setMateria(materia);
        alumnoMateria.setEstado(EstadoMateria.PENDIENTE);
        alumnoMateria.setFechaInscripcion(LocalDate.now());

        return alumnoMateriaMapper.toDTO(alumnoMateriaRepository.save(alumnoMateria));

    }

    public AlumnoMateriaDTO modificar(Long id, AlumnoMateriaDTO dto) {
        AlumnoMateria alumnoMateria = buscarPorId(id);
        alumnoMateriaMapper.actualizarEntity(dto, alumnoMateria);
        alumnoMateria.setAlumno(buscarAlumno(dto.getIdAlumno()));
        alumnoMateria.setMateria(buscarMateria(dto.getIdMateria()));
        return alumnoMateriaMapper.toDTO(alumnoMateriaRepository.save(alumnoMateria));
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        alumnoMateriaRepository.deleteById(id);
    }

    private Alumno buscarAlumno(Long idAlumno) {
        return alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new AlumnoInvalidoException("El alumno con id " + idAlumno + " no existe"));
    }

    private Materia buscarMateria(Long idMateria) {
        return materiaRepository.findById(idMateria)
                .orElseThrow(() -> new MateriaInexistente("La materia con id " + idMateria + " no existe"));
    }

    public AlumnoMateriaDTO pasarAcursando(Long idAlumno, Long idMateria)
    {
        Alumno alumno = buscarAlumno(idAlumno);
        Materia materia = buscarMateria(idMateria);

        AlumnoMateria alumnoMateria = alumnoMateriaRepository.findByAlumnoAndMateria(alumno,materia).orElseThrow(() -> new EntidadNoEncontradaException("No inscripto"));

        if(alumnoMateria.getEstado() == EstadoMateria.APROBADA)
        {
            return alumnoMateriaMapper.toDTO(alumnoMateria);
        }

        alumnoMateria.setEstado(EstadoMateria.CURSANDO);

        return alumnoMateriaMapper.toDTO(alumnoMateriaRepository.save(alumnoMateria));
    }

    public AlumnoMateriaDTO registrarParciales(Long idAlumno, Long idMateria, double n1, double n2)
    {
        Alumno alumno = buscarAlumno(idAlumno);
        Materia materia = buscarMateria(idMateria);

        AlumnoMateria am = alumnoMateriaRepository.findByAlumnoAndMateria(alumno,materia).orElseThrow(() -> new EntidadNoEncontradaException("no inscripto"));

        if(am.getEstado() == EstadoMateria.APROBADA)
        {
            return alumnoMateriaMapper.toDTO(am);
        }
        am.setNotaParcial1(n1);
        am.setNotaParcial2(n2);

        if (n1 >= 6 && n2 >= 6)
        {
            am.setEstado(EstadoMateria.REGULAR);
            am.setFechaRegularizacion(LocalDate.now());
        }else
        {
            am.setEstado(EstadoMateria.CURSANDO);
            am.setFechaRegularizacion(null);
        }

        return alumnoMateriaMapper.toDTO(alumnoMateriaRepository.save(am));
    }


    public AlumnoMateriaDTO aprobarFinal(Long idAlumno, Long idMateria, double notaFinal)
    {
        Alumno alumno = buscarAlumno(idAlumno);
        Materia materia = buscarMateria(idMateria);

        AlumnoMateria am = alumnoMateriaRepository.findByAlumnoAndMateria(alumno,materia).orElseThrow(() -> new EntidadNoEncontradaException("No inscripto"));

        if (am.getEstado() != EstadoMateria.REGULAR)
        {
            return alumnoMateriaMapper.toDTO(am);
        }

        am.setNotaFinal(notaFinal);

        if (notaFinal >= 4)
        {
            am.setEstado(EstadoMateria.APROBADA);
            am.setFechaAprobacion(LocalDate.now());
        }

        return alumnoMateriaMapper.toDTO(alumnoMateriaRepository.save(am));
    }

}