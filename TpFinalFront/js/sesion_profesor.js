// ============================================================
// Sesion del profesor (compartida por todas las paginas del profesor)
// ------------------------------------------------------------
// El login real (token JWT) lo maneja Auth en api.js. Este archivo guarda el
// idProfesor (idPersona) que el backend necesita y que el token NO trae:
//   - idProfesor -> GET /comisiones/profesor/{id}, /profesores/{id}/comisiones,
//                   /profesores/{id}/comisiones/{comisionId}/alumnos, etc.
//
// Se sigue pidiendo a mano porque el JWT solo lleva el email y no hay endpoint
// que, con rol PROFESOR, devuelva el idPersona del profesor logueado. Cuando el
// backend agregue un /me (ver NOTAS_BACKEND.txt), sale del token.
//
// El token + el idProfesor se cargan en login_profesor.js.
// (API_BASE y los helpers viven en api.js, que se carga antes.)
// ============================================================

const SesionProfesor = {
    guardar(idProfesor) {
        sessionStorage.setItem("profesor_id", idProfesor);
    },
    idProfesor() {
        return sessionStorage.getItem("profesor_id");
    },
    cerrar() {
        sessionStorage.removeItem("profesor_id");
        Auth.cerrar();
    },
    requerir() {
        if (!Auth.haySesion() || !this.idProfesor()) {
            window.location.href = "/pages/Profesor/login_profesor.html";
        }
    },
};

// ============================================================
// Boton "Salir" de la navbar (presente en todas las paginas del profesor)
// ============================================================
document.getElementById("nav-salir")?.addEventListener("click", (evento) => {
    evento.preventDefault();
    SesionProfesor.cerrar();
    window.location.href = "/pages/Profesor/login_profesor.html";
});
