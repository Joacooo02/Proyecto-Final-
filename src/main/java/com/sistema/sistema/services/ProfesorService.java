package com.sistema.sistema.services;

import com.sistema.sistema.Security.User.User;
import com.sistema.sistema.Security.User.UserRepository;
import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoProfesor;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.enums.RolUsuario;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.exceptions.EntidadDuplicadaException;
import com.sistema.sistema.mappers.AlumnoMapper;
import com.sistema.sistema.repositories.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    @Autowired
    private final ProfesorRepository profesorRepository;

    @Autowired
    private final ComisionRepository comisionRepository;

    @Autowired
    private final AlumnoRepository alumnoRepository;

    @Autowired
    private final AlumnoMapper alumnoMapper;

    @Autowired
    private final ExamenRepository examenRepository;

    @Autowired
    private final AlumnoInscripcionExamenFinalRepository alumnoInscripcionExamenFinalRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Profesor buscarProfesorPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Profesor con id: " + id + " no encontrado"));
    }

    public Profesor agregarProfesor(Profesor profesor) {
        if (profesor.getEmail() == null || profesor.getEmail().isBlank()) {
            throw new EntidadDuplicadaException("Debe indicarse un email para crear el usuario de acceso");
        }
        if (userRepository.findByEmail(profesor.getEmail()).isPresent()) {
            throw new EntidadDuplicadaException("Ya existe un usuario con email: " + profesor.getEmail());
        }

        profesor.setRolUsuario(RolUsuario.PROFESOR);

        // Usuario de acceso: login por email, contraseña inicial = DNI (la cambia luego el profesor).
        // El cascade ALL de Persona.user persiste el User junto con el profesor.
        User user = User.builder()
                .username(profesor.getEmail())
                .email(profesor.getEmail())
                .password(passwordEncoder.encode(profesor.getDni()))
                .role(RolUsuario.PROFESOR)
                .build();
        profesor.setUser(user);

        return profesorRepository.save(profesor);
    }

    public void eliminarProfesor(Long id) {
        buscarProfesorPorId(id);
        profesorRepository.deleteById(id);
    }

    public List<Profesor> listarProfesores(String nombre, String apellido, String dni, String email, EstadoProfesor estado) {
        return profesorRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }
            if (apellido != null && !apellido.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%"));
            }
            if (dni != null && !dni.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("dni"), dni));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoProfesor"), estado));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Profesor modificarProfesor(Long id, Profesor profesorModificado) {
        Profesor profesorExistente = buscarProfesorPorId(id);

        profesorExistente.setNombre(profesorModificado.getNombre());
        profesorExistente.setApellido(profesorModificado.getApellido());
        profesorExistente.setDni(profesorModificado.getDni());
        profesorExistente.setTelefono(profesorModificado.getTelefono());
        profesorExistente.setFechaNacimiento(profesorModificado.getFechaNacimiento());
        profesorExistente.setEmail(profesorModificado.getEmail());

        profesorExistente.setHorasSemanales(profesorModificado.getHorasSemanales());
        profesorExistente.setEstadoProfesor(profesorModificado.getEstadoProfesor());

        return profesorRepository.save(profesorExistente);
    }

    public List<ComisionDTO> obtenerComisionesProfesor(Long profesorId) {
        List<Comision> comisiones = comisionRepository.findByProfesor_IdPersona(profesorId);

        return comisiones.stream()
                .map(comision -> ComisionDTO.builder()
                        .idComision(comision.getIdComision())
                        .nroComision(comision.getNroComision())
                        .aula(comision.getAula())
                        .cantAlumnos(comision.getCantAlumnos())
                        .materiaNombre(comision.getMateria() != null ? comision.getMateria().getNombre() : null)
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<MateriaDTO> obtenerMateriasProfesor(Long profesorId)
    {
        return comisionRepository.findByProfesor_IdPersona(profesorId)
                .stream()
                .map(comision -> MateriaDTO.builder()
                        .id(comision.getMateria().getIdMateria())
                        .nombre(comision.getMateria().getNombre())
                        .cargaHoraria(comision.getMateria().getCargaHoraria())
                        .cuatrimestre(comision.getMateria().getCuatrimestre())
                        .anioCursado(comision.getMateria().getAnioCursado())
                        .build()
                )
                .distinct()
                .toList();
    }

    public List<AlumnoDTO> obtenerAlumnosMateria(Long materiaId) {
        List<Alumno> alumnosInscriptos = alumnoRepository
                .findByInscripcionesMateria_Materia_IdMateria(materiaId);
        return alumnoMapper.toDTOList(alumnosInscriptos);
    }

    public List<AlumnoDTO> obtenerAlumnosInscriptosComision(Long profesorId, Long comisionId) {
        Comision comision = comisionRepository.findById(comisionId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Comisión con id: " + comisionId + " no encontrada"));

        if (comision.getProfesor() == null || !comision.getProfesor().getIdPersona().equals(profesorId)) {
            throw new EntidadNoEncontradaException("El profesor con id: " + profesorId + " no tiene a cargo la comisión con id: " + comisionId);
        }

        List<Alumno> alumnos = alumnoRepository.findDistinctByInscripcionComision_Comision_IdComision(comisionId);
        return alumnoMapper.toDTOList(alumnos);
    }

    public List<AlumnoDTO> obtenerAlumnosInscriptosExamenFinal(Long profesorId, Long examenId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Examen con id: " + examenId + " no encontrado"));

        if (!profesorDictaMateria(profesorId, examen.getMateria().getIdMateria())) {
            throw new EntidadNoEncontradaException("El profesor con id: " + profesorId + " no dicta la materia asociada al examen con id: " + examenId);
        }

        List<Alumno> alumnos = alumnoInscripcionExamenFinalRepository.findByExamen_IdExamen(examenId)
                .stream()
                .map(AlumnoInscripcionExamenFinal::getAlumno)
                .distinct()
                .collect(Collectors.toList());

        return alumnoMapper.toDTOList(alumnos);
    }

    public List<AlumnoDTO> obtenerAlumnosInscriptosMateria(Long profesorId, Long materiaId) {
        if (!profesorDictaMateria(profesorId, materiaId)) {
            throw new EntidadNoEncontradaException("El profesor con id: " + profesorId + " no dicta la materia con id: " + materiaId);
        }

        List<Alumno> alumnos = alumnoRepository.findDistinctByInscripcionComision_Comision_Materia_IdMateria(materiaId);
        return alumnoMapper.toDTOList(alumnos);
    }

    private boolean profesorDictaMateria(Long profesorId, Long materiaId) {
        return comisionRepository.findByProfesor_IdPersona(profesorId)
                .stream()
                .anyMatch(comision -> comision.getMateria().getIdMateria().equals(materiaId));
    }
}

