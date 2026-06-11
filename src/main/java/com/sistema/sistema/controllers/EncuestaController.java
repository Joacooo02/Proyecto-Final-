package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.EncuestaRequest;
import com.sistema.sistema.entities.encuestas.PreguntaEncuesta;
import com.sistema.sistema.entities.encuestas.RespuestaEncuesta;
import com.sistema.sistema.services.EncuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/encuestas")
@RequiredArgsConstructor
public class EncuestaController {

    private final EncuestaService encuestaService;

    @GetMapping("/preguntas")
    public List<PreguntaEncuesta> listarPreguntas() {
        return encuestaService.listarPreguntas();
    }

    @GetMapping("/{idComision}/ya-respondio/{idAlumno}")
    public boolean yaRespondio(@PathVariable Long idComision, @PathVariable Long idAlumno) {
        return encuestaService.yaRespondio(idComision, idAlumno);
    }

    @PostMapping("/{idComision}/responder/{idAlumno}")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaEncuesta responderEncuesta(
            @PathVariable Long idComision,
            @PathVariable Long idAlumno,
            @RequestBody EncuestaRequest request) {
        return encuestaService.responderEncuesta(
                idComision, idAlumno,
                request.getRespuestas(),
                request.getComentarioFinal());
    }

    @GetMapping("/{idComision}/resultados")
    public Map<String, Double> obtenerResultados(@PathVariable Long idComision) {
        return encuestaService.obtenerResultados(idComision);
    }
}