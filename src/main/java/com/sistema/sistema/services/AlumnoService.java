package com.sistema.sistema.services;

import com.sistema.sistema.Security.User.User;
import com.sistema.sistema.Security.User.UserRepository;
import com.sistema.sistema.exceptions.BoletoException;
import com.sistema.sistema.dto.*;
import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarrera;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarreraId;
import com.sistema.sistema.entities.funcionalidades.BoletoEspecialEducativo;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.RolUsuario;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.AlumnoMapper;
import com.sistema.sistema.repositories.*;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
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
    private final CorrelatividadService correlatividadService;
    private final MateriaRepository materiaRepository;
    private final CarreraRepository carreraRepository;
    private final AlumnoCursaCarreraRepository alumnoCursaCarreraRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AlumnoDTO buscarAlumnoPorLegajo(Long legajo) {

        String emailLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        var autoridades = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean esAlumno = autoridades.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUMNO"));

        if (esAlumno)
        {
            Alumno alumnoLogueado = alumnoRepository.findByEmail(emailLogueado).orElseThrow(() -> new EntidadNoEncontradaException("Alumno no encontrado con el email: " + emailLogueado));


            if (!alumnoLogueado.getLegajo().equals(legajo)) {
                throw new AlumnoInvalidoException("No tenés permiso para ver los datos de otro alumno.");
            }


        }


        return alumnoMapper.toDTO(obtenerAlumnoPorLegajo(legajo));
    }

    // Devuelve los datos del alumno a partir de su email (el que viaja en el JWT).
    // Lo usa GET /alumnos/me para que el front no tenga que pedir legajo/idPersona en el login.
    public AlumnoDTO buscarAlumnoPorEmail(String email){
        Alumno alumno = alumnoRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadNoEncontradaException("Alumno con email: " + email + " no encontrado"));
        return alumnoMapper.toDTO(alumno);
    }

    @Transactional
    public AlumnoDTO agregarAlumno(AltaAlumnoDTO altaAlumnoDTO){
        AlumnoDTO alumnoDTO = altaAlumnoDTO.getAlumno();

        if (altaAlumnoDTO.getIdCarrera() == null) {
            throw new AlumnoInvalidoException("Debe indicarse la carrera a la que se inscribe el alumno");
        }
        Carrera carrera = carreraRepository.findById(altaAlumnoDTO.getIdCarrera())
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Carrera con id: " + altaAlumnoDTO.getIdCarrera() + " no encontrada"));

        if (alumnoDTO.getLegajo() != null && alumnoRepository.findByLegajo(alumnoDTO.getLegajo()).isPresent()) {
            throw new AlumnoInvalidoException("Ya existe un alumno con legajo: " + alumnoDTO.getLegajo());
        }

        if (alumnoDTO.getDni() != null && alumnoRepository.findByDni(alumnoDTO.getDni()).isPresent()) {
            throw new AlumnoInvalidoException(
                    "Ya existe un alumno con DNI: " + alumnoDTO.getDni());
        }

        if (alumnoDTO.getEmail() == null || alumnoDTO.getEmail().isBlank()) {
            throw new AlumnoInvalidoException("Debe indicarse un email para crear el usuario de acceso");
        }
        if (userRepository.findByEmail(alumnoDTO.getEmail()).isPresent()) {
            throw new AlumnoInvalidoException("Ya existe un usuario con email: " + alumnoDTO.getEmail());
        }

        Alumno alumno = alumnoMapper.toEntity(alumnoDTO);
        alumno.setRolUsuario(RolUsuario.ALUMNO);

        // Usuario de acceso: login por email, contraseña inicial = DNI (la cambia luego el alumno).
        // El cascade ALL de Persona.user persiste el User junto con el alumno.
        User user = User.builder()
                .username(alumnoDTO.getEmail())
                .email(alumnoDTO.getEmail())
                .password(passwordEncoder.encode(alumnoDTO.getDni()))
                .role(RolUsuario.ALUMNO)
                .build();
        alumno.setUser(user);

        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        AlumnoCursaCarrera inscripcion = AlumnoCursaCarrera.builder()
                .id(new AlumnoCursaCarreraId())
                .alumno(alumnoGuardado)
                .carrera(carrera)
                .fecha_inscripcion(LocalDate.now())
                .build();
        alumnoCursaCarreraRepository.save(inscripcion);

        return alumnoMapper.toDTO(alumnoGuardado);
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

        return alumno.getInscripcionesMateria()
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

    public void registrarBoleto(Long id)
    {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));

        if (boletoEspecialEducativoRepository.existsByAlumno(alumno)) {
            throw new BoletoException("El alumno ya posee una solicitud de boleto");
        }

        BoletoEspecialEducativo boletoEspecialEducativo = new BoletoEspecialEducativo();

        boletoEspecialEducativo.setAlumno(alumno);
        boletoEspecialEducativo.setFueSolicitado(true);

        Random generador = new Random();
        boletoEspecialEducativo.setEstaActivo(generador.nextBoolean());
        boletoEspecialEducativoRepository.save(boletoEspecialEducativo);

    }

    public boolean tieneBoletoActivo(Long id)
    {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new AlumnoInvalidoException("Alumno no encontrado"));
        BoletoEspecialEducativo boleto = boletoEspecialEducativoRepository.findByAlumno(alumno).orElseThrow(() -> new BoletoException("El alumno no posee boleto"));

        return boleto.getEstaActivo();
    }
}