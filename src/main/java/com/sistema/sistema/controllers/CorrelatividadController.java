package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.CorrelatividadDTO;
import com.sistema.sistema.services.CorrelatividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/correlatividades")
@RequiredArgsConstructor
public class CorrelatividadController {
    private final CorrelatividadService correlatividadService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public CorrelatividadDTO crear(@RequestBody CorrelatividadDTO dto)
    {
        return correlatividadService.crear(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<CorrelatividadDTO> listar()
    {
        return correlatividadService.listar();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/materia/{idMateria}")
    public List<CorrelatividadDTO> buscarPorMateria(@PathVariable Long idMateria)
    {
        return correlatividadService.buscarPorMateria(idMateria);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id)
    {
        correlatividadService.eliminar(id);
    }
}
