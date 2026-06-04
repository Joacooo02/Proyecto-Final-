package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.services.ExamenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examenes")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenService examenService;

    @PostMapping("/materia/{idMateria}")
    public Examen agregarExamen(@PathVariable Long idMateria, @RequestBody Examen examen)
    {
        return examenService.agregarExamen(idMateria, examen);
    }
}
