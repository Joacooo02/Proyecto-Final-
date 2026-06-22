// ============================================================
// Login del alumno  (JWT real)
// ------------------------------------------------------------
// POST /auth/login con { email, password }. El usuario del alumno se crea solo
// cuando el admin lo da de alta (POST /alumnos): usuario = email, contraseña
// INICIAL = su DNI (despues la puede cambiar). Ver AlumnoService.
//
// El login pide SOLO email + contraseña. El legajo y el idAlumno (idPersona) que
// el resto del portal necesita YA NO se piden a mano: apenas autenticamos, el
// front llama GET /alumnos/me (rol ALUMNO; el email sale del token) y de ahi saca
// legajo + idPersona, que se guardan en SesionAlumno. Ver AlumnoController.
// ============================================================

const formLogin = document.getElementById("form-login");

formLogin.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    ocultarMensaje();

    const datos = Object.fromEntries(new FormData(formLogin));

    // CORREGIDO: Ahora se usa datos.password porque viene del HTML con ese name
    if (!datos.email || !datos.password) {
        mostrarMensaje("Completa email y contraseña.");
        return;
    }

    try {
        // CORREGIDO: Desestructuración con las llaves correctas de api.js
        const { acces_token, refresh_token } = await loginJwt(datos.email, datos.password);
        Auth.guardarSesion({ token: acces_token, refresh: refresh_token, rol: "ALUMNO" });

        // Sesion ya guardada -> el fetch parcheado (api.js) le mete el Bearer. /alumnos/me
        // devuelve el alumno del email del token; de ahi salen legajo + idPersona.
        const respuesta = await fetch(API_BASE + "/alumnos/me");
        if (!respuesta.ok) {
            Auth.cerrar();
            throw new Error(await extraerMensajeError(respuesta));
        }
        const alumno = await respuesta.json();
        SesionAlumno.guardar(alumno.legajo, alumno.idPersona);

        window.location.href = "/pages/Alumno/menu_alumno.html";
    } catch (error) {
        mostrarMensaje("No se pudo iniciar sesion: " + error.message);
    }
});