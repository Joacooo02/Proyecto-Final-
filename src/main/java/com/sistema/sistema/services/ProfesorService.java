package com.sistema.sistema.services;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoProfesor;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.AlumnoMapper;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.ComisionRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import com.sistema.sistema.repositories.ProfesorRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Profesor buscarProfesorPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Profesor con id: " + id + " no encontrado"));
    }

    public Profesor agregarProfesor(Profesor profesor) {
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
}

