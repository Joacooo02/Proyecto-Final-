package com.sistema.sistema.services;

import com.sistema.sistema.Exceptions.AvisoException;
import com.sistema.sistema.entities.areaAdministrativa.Aviso;
import com.sistema.sistema.dto.AvisoDTO;
import com.sistema.sistema.entities.usuario.Persona;
import com.sistema.sistema.enums.RolUsuario;
import com.sistema.sistema.mappers.AvisoMapper;
import com.sistema.sistema.repositories.AvisoRepository;
import com.sistema.sistema.repositories.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvisoService {

    private final AvisoRepository avisoRepository;
    private final PersonaRepository personaRepository;
    private final AvisoMapper avisoMapper;

    public AvisoDTO crearAviso(Long idPersona, AvisoDTO dto) {
        Persona persona = personaRepository.findById(idPersona).orElseThrow(() -> new RuntimeException("No se enconntro a la persona"));

        if(persona.getRolUsuario() != RolUsuario.PROFESOR && persona.getRolUsuario() != RolUsuario.ADMIN)
        {
            throw new RuntimeException("No tiene permiso para crear un avios");
        }

        Aviso aviso = Aviso.builder()
                .persona(persona)
                .titulo(dto.getTitulo())
                .contenido(dto.getContenido())
                .fechaAviso(LocalDateTime.now())
                .build();
        aviso = avisoRepository.save(aviso);

        return avisoMapper.toDTO(aviso);
    }

    public List<AvisoDTO> listarAvisoDto()
    {
        return avisoMapper.toDTO(avisoRepository.findAll());
    }

    public AvisoDTO verAvisoPorId(Long idAviso)
    {
        Aviso aviso = avisoRepository.findById(idAviso).orElseThrow(() -> new RuntimeException("No se encontro el aviso"));
        return avisoMapper.toDTO(aviso);
    }

    public void eliminarAvisoPorId(Long idPersona, Long idAviso) {

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró a la persona"));

        if (persona.getRolUsuario() != RolUsuario.PROFESOR &&
                persona.getRolUsuario() != RolUsuario.ADMIN) {

            throw new RuntimeException("No tiene permisos para eliminar avisos");
        }

        Aviso aviso = avisoRepository.findById(idAviso)
                .orElseThrow(() -> new RuntimeException("No se encontró el aviso"));

        avisoRepository.delete(aviso);
    }

    public AvisoDTO modificarAviso(Long idAviso, Long idPersona, AvisoDTO dto)
    {
        Persona persona = personaRepository.findById(idPersona).orElseThrow(() -> new AvisoException("No se encontró a la persona"));

        if (persona.getRolUsuario() != RolUsuario.PROFESOR && persona.getRolUsuario() != RolUsuario.ADMIN)
        {
            throw new AvisoException("No tiene permisos para modificar el aviso");
        }

        Aviso aviso = avisoRepository.findById(idAviso).orElseThrow(() -> new RuntimeException("No se encontró el aviso"));

        aviso.setTitulo(dto.getTitulo());
        aviso.setContenido(dto.getContenido());

        aviso = avisoRepository.save(aviso);
        return avisoMapper.toDTO(aviso);
    }
}
