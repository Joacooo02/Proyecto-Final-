package com.sistema.sistema.services;

import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarrera;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.MateriaMapper;
import com.sistema.sistema.repositories.AlumnoCursaCarreraRepository;
import com.sistema.sistema.repositories.CarreraRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final AlumnoCursaCarreraRepository alumnoCursaCarreraRepository;
    private final MateriaMapper materiaMapper;
    private final CarreraRepository carreraRepository;

    private Materia obtenerMateria(Long id)
    {
        return materiaRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Materia con id: " + id + " no encontrada"));
    }

    public MateriaDTO buscarMateriaPorId(Long id)
    {
        return materiaMapper.toDTO(materiaRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Materia con id: " + id + " no encontrada")));
    }

    public MateriaDTO agregarMateria(MateriaDTO materiaDTO)
    {

        Carrera carrera = carreraRepository.findById(materiaDTO.getIdCarrera())
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Carrera no encontrada"));

        Materia materia = materiaMapper.toEntity(materiaDTO);

        materia.setCarrera(carrera);

        return materiaMapper.toDTO(
                materiaRepository.save(materia)
        );
    }

    public void eliminarMateria(Long id) {
        buscarMateriaPorId(id);
        materiaRepository.deleteById(id);
    }

    public List<MateriaDTO> listarMaterias(String nombre)
    {
        List<Materia> materias;

        if (nombre != null && !nombre.isBlank()) {
            materias = materiaRepository.findByNombreContainingIgnoreCase(nombre);
        } else {
            materias = materiaRepository.findAll();
        }

        return materiaMapper.toDTOList(materias);
    }

    public MateriaDTO modificarMateria(Long id, MateriaDTO dto)
    {

        Materia materia = obtenerMateria(id);

        materiaMapper.actualizarEntity(dto, materia);

        if(dto.getIdCarrera() != null)
        {
            Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                    .orElseThrow(() ->
                            new EntidadNoEncontradaException("Carrera no encontrada"));

            materia.setCarrera(carrera);
        }

        return materiaMapper.toDTO(
                materiaRepository.save(materia)
        );
    }

    public List<Materia> verPlanAcademicoAlumno(Long idAlumno)
    {
        AlumnoCursaCarrera inscripcion = alumnoCursaCarreraRepository.findByAlumnoIdPersona(idAlumno).orElseThrow(() -> new EntidadNoEncontradaException("El alumno no esta inscripto en ninguna carrera"));
        Long idCarrera = inscripcion.getCarrera().getIdCarrera();

        return materiaRepository.findByCarreraIdCarrera(idCarrera);
    }






}
