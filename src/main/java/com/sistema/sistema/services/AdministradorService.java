package com.sistema.sistema.services;

import com.sistema.sistema.entities.usuario.Administrador;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public Administrador buscarAdministradorPorId(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Administrador con id: " + id + " no encontrado"));
    }

    public Administrador agregarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public void eliminarAdministrador(Long id) {
        buscarAdministradorPorId(id);
        administradorRepository.deleteById(id);
    }

    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    public Administrador modificarAdministrador(Long id, Administrador administradorModificado) {

        Administrador administradorExistente = buscarAdministradorPorId(id);

        administradorExistente.setNombre(administradorModificado.getNombre());
        administradorExistente.setApellido(administradorModificado.getApellido());
        administradorExistente.setDni(administradorModificado.getDni());
        administradorExistente.setTelefono(administradorModificado.getTelefono());
        administradorExistente.setFechaNacimiento(administradorModificado.getFechaNacimiento());
        administradorExistente.setEmail(administradorModificado.getEmail());

        return administradorRepository.save(administradorExistente);
    }
}
