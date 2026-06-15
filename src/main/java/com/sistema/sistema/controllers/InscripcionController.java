package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscripcion")
public class InscripcionController {

    @Autowired
    private InscripcionService service;


    @PostMapping("/comision/{idAlumno}/{idComision}")
    public AlumnoInscripcionComision inscribirComision(@PathVariable Long idAlumno, @PathVariable Long idComision)
    {
        return service.inscribirComision(idAlumno, idComision);
    }

    @PostMapping("/examenFinal/{idAlumno}/{idExamen}")
    public AlumnoInscripcionExamenFinal inscribirExamen(@PathVariable Long idAlumno, @PathVariable Long idExamen)
    {
        return service.inscribirExamen(idAlumno, idExamen);
    }

    @GetMapping("/materia/{idMateria}/comisiones-disponibles")
    public List<Comision> obtenerComisionesDisponibles(@PathVariable Long idMateria)
    {
        return service.obtenerComisionesDisponibles(idMateria);
    }

    @PostMapping("/materia/{idAlumno}/{idMateria}")
    public AlumnoInscripcionMateria inscribirMateria(
            @PathVariable Long idAlumno,
            @PathVariable Long idMateria)
    {
        return service.inscribirMateria(idAlumno,idMateria);
    }
}
