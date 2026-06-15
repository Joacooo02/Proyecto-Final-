package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.CorrelatividadDTO;
import com.sistema.sistema.services.CorrelatividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/correlatividades")
@RequiredArgsConstructor
public class CorrelatividadController {
    private final CorrelatividadService correlatividadService;

    @PostMapping
    public CorrelatividadDTO crear(@RequestBody CorrelatividadDTO dto)
    {
        return correlatividadService.crear(dto);
    }

    @GetMapping
    public List<CorrelatividadDTO> listar()
    {
        return correlatividadService.listar();
    }

    @GetMapping("/materia/{idMateria}")
    public List<CorrelatividadDTO> buscarPorMateria(@PathVariable Long idMateria)
    {
        return correlatividadService.buscarPorMateria(idMateria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id)
    {
        correlatividadService.eliminar(id);
    }
}
