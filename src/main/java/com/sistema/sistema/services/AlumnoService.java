package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.dto.HistorialAcademicoDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.funcionalidades.BoletoEspecialEducativo;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.RolUsuario;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.AlumnoMapper;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.BoletoEspecialEducativoRepository;
import com.sistema.sistema.repositories.NotaRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final NotaRepository notaRepository;
    private final AlumnoMapper alumnoMapper;
    private final BoletoEspecialEducativoRepository boletoEspecialEducativoRepository;

    public AlumnoDTO buscarAlumnoPorLegajo(Long legajo){
        return alumnoMapper.toDTO(obtenerAlumnoPorLegajo(legajo));
    }

    public AlumnoDTO agregarAlumno(AlumnoDTO alumnoDTO){
        Alumno alumno = alumnoMapper.toEntity(alumnoDTO);
        alumno.setRolUsuario(RolUsuario.ALUMNO);
        return alumnoMapper.toDTO(alumnoRepository.save(alumno));
    }

    public void eliminarAlumno(Long legajo){
        Alumno alumno = obtenerAlumnoPorLegajo(legajo);
        alumnoRepository.delete(alumno);
    }

    public List<AlumnoDTO> listarAlumnos(String nombre, String apellido, String dni, String email, Long legajo) {
        List<Alumno> alumnos = alumnoRepository.findAll((root, query, criteriaBuilder) -> {
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
        return alumnoMapper.toDTOList(alumnos);
    }

    public AlumnoDTO modificarAlumno(Long legajo, AlumnoDTO alumnoModificado) {
        Alumno alumnoExistente = obtenerAlumnoPorLegajo(legajo);
        alumnoMapper.actualizarEntity(alumnoModificado, alumnoExistente);
        return alumnoMapper.toDTO(alumnoRepository.save(alumnoExistente));
    }

    public List<HistorialAcademicoDTO> verHistorialAcademicoAlumno(Long legajo)
    {
        Alumno alumno = obtenerAlumnoPorLegajo(legajo);
        List<Nota> notas = notaRepository.findByAlumnoIdPersona(alumno.getIdPersona());
        List<HistorialAcademicoDTO> historial = new ArrayList<>();

        for(Nota nota : notas)
        {
            String estadoMateria;

            if(nota.getNota() >= 6)
            {
                estadoMateria = "APROBADO";
            }else
            {
                estadoMateria = "DESAPROBADO";
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

    public List<MateriaDTO> obtenerMaterias(Long legajo) {

        Alumno alumno = alumnoRepository.findByLegajo(legajo)
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

    private Alumno obtenerAlumnoPorLegajo(Long legajo){
        return alumnoRepository.findByLegajo(legajo)
                .orElseThrow(()-> new EntidadNoEncontradaException("Alumno con legajo: " +legajo+ " no encontrado"));
    }

    public BoletoEspecialEducativo registrarBoleto(Long id) {
        Random generador = new Random();

        BoletoEspecialEducativo boletoEspecialEducativo = new BoletoEspecialEducativo();
        boletoEspecialEducativo.setAlumnoId(id);
        boletoEspecialEducativo.setFueSolicitado(true);
        boletoEspecialEducativo.setEstaActivo(generador.nextBoolean());

        return boletoEspecialEducativoRepository.save(boletoEspecialEducativo);
    }
}