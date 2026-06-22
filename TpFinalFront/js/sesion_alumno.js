// ============================================================
// Sesion del alumno (compartida por todas las paginas del alumno)
// ------------------------------------------------------------
// El login real (token JWT) lo maneja Auth en api.js. Este archivo guarda los
// IDENTIFICADORES del alumno que el backend necesita y que el token NO trae:
//
//   - legajo               -> /alumnos/{legajo}/historial, /alumnos/{legajo}/materias, PUT /alumnos/{legajo}
//   - idAlumno (idPersona) -> inscripciones, cuotas, pagos, encuestas, boleto, plan academico
//
// ¿Por que se siguen pidiendo a mano? Porque el JWT solo lleva el email del
// usuario y el AlumnoDTO no expone el idPersona; ademas no hay endpoint /me ni
// uno que, con rol ALUMNO, devuelva legajo/idPersona a partir del email. Cuando
// el backend agregue eso (ver NOTAS_BACKEND.txt), esto se saca del token y listo.
//
// El token + estos ids se cargan en login_alumno.js.
// (API_BASE y los helpers mostrarMensaje/extraerMensajeError/formatearFecha/siNo
//  viven en api.js, que se carga antes.)
// ============================================================

const SesionAlumno = {
    guardar(legajo, idAlumno) {
        sessionStorage.setItem("alumno_legajo", legajo);
        sessionStorage.setItem("alumno_id", idAlumno);
    },
    legajo() {
        return sessionStorage.getItem("alumno_legajo");
    },
    idAlumno() {
        return sessionStorage.getItem("alumno_id");
    },
    cerrar() {
        sessionStorage.removeItem("alumno_legajo");
        sessionStorage.removeItem("alumno_id");
        Auth.cerrar();
    },
    // Si no hay token o faltan los ids, vuelve al login. Se llama al inicio de cada pagina.
    requerir() {
        if (!Auth.haySesion() || !this.legajo() || !this.idAlumno()) {
            window.location.href = "/pages/Alumno/login_alumno.html";
        }
    },
};

// ============================================================
// Boton "Salir" de la navbar (presente en todas las paginas del alumno)
// ============================================================
document.getElementById("nav-salir")?.addEventListener("click", (evento) => {
    evento.preventDefault();
    SesionAlumno.cerrar();
    window.location.href = "/pages/Alumno/login_alumno.html";
});
