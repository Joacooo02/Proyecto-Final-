package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.entities.dto.HistorialAcademicoDTO;
import com.sistema.sistema.entities.dto.MateriaDTO;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.NotaRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final NotaRepository notaRepository;

    public Alumno buscarAlumnoPorId(Long id){
       return alumnoRepository.findById(id)
                .orElseThrow(()-> new EntidadNoEncontradaException("Alumno con id: " +id+ " no encontrado"));
    }

    public Alumno agregarAlumno(Alumno alumno){
        return alumnoRepository.save(alumno);
    }

    public void eliminarAlumno(Long id){
        buscarAlumnoPorId(id);
        alumnoRepository.deleteById(id);
    }

    public List<Alumno> listarAlumnos(String nombre, String apellido, String dni, String email, Long legajo) {
        return alumnoRepository.findAll((root, query, criteriaBuilder) -> {
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
            if (legajo != null) {
                predicates.add(criteriaBuilder.equal(root.get("legajo"), legajo));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Alumno modificarAlumno(Long id, Alumno alumnoModificado) {

        Alumno alumnoExistente = buscarAlumnoPorId(id);

        alumnoExistente.setNombre(alumnoModificado.getNombre());
        alumnoExistente.setApellido(alumnoModificado.getApellido());
        alumnoExistente.setDni(alumnoModificado.getDni());
        alumnoExistente.setTelefono(alumnoModificado.getTelefono());
        alumnoExistente.setFechaNacimiento(alumnoModificado.getFechaNacimiento());
        alumnoExistente.setEmail(alumnoModificado.getEmail());

        alumnoExistente.setLegajo(alumnoModificado.getLegajo());
        alumnoExistente.setAnioIngreso(alumnoModificado.getAnioIngreso());
        alumnoExistente.setAnaliticoParcial(alumnoModificado.isAnaliticoParcial());
        alumnoExistente.setEsRegular(alumnoModificado.isEsRegular());
        alumnoExistente.setPlanEstudio(alumnoModificado.getPlanEstudio());
        alumnoExistente.setPromedio(alumnoModificado.getPromedio());

        return alumnoRepository.save(alumnoExistente);
    }

    public List<HistorialAcademicoDTO> verHistorialAcademicoAlumno(Long idAlumno)
    {
        List<Nota> notas = notaRepository.findByAlumnoIdPersona(idAlumno);
        List<HistorialAcademicoDTO> historial = new ArrayList<>();

        for(Nota nota : notas)
        {
            String estadoMateria;

            if(nota.getNota() >= 6)
            {
                estadoMateria = "APROBADA";
            }else
            {
                estadoMateria = "DESAPROBADA";
            }
            historial.add(new HistorialAcademicoDTO(
                    nota.getExamen().getMateria().getNombre(),
                    nota.getExamen().getTipoExamen().toString(),
                    nota.getNota(),
                    nota.getExamen().getFecha(),
                    estadoMateria
            ));
        }
        return historial;
    }

    public List<MateriaDTO> obtenerMaterias(Long idAlumno) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new AlumnoInvalidoException("no existe el alumno"));

        return alumno.getInscripcionMateriaList()
                .stream()
                .map(ins -> MateriaDTO.builder()
                        .id(ins.getMateria().getIdMateria())
                        .nombre(ins.getMateria().getNombre())
                        .cargaHoraria(ins.getMateria().getCargaHoraria())
                        .cuatrimestre(ins.getMateria().getCuatrimestre())
                        .anioCursado(ins.getMateria().getAnioCursado())
                        .build()
                )
                .toList();
    }

}
