package com.sistema.sistema.services;

import com.sistema.sistema.dto.AlumnoMateriaDTO;
import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.mappers.AlumnoMateriaMapper;
import com.sistema.sistema.repositories.AlumnoMateriaRepository;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        AlumnoMateria alumnoMateria = alumnoMateriaMapper.toEntity(dto);
        alumnoMateria.setAlumno(buscarAlumno(dto.getIdAlumno()));
        alumnoMateria.setMateria(buscarMateria(dto.getIdMateria()));
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
}