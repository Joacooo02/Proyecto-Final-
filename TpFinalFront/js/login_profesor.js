// ============================================================
// Login del profesor  (JWT real)
// ------------------------------------------------------------
// POST /auth/login con { email, password }. El usuario del profesor se crea
// cuando el admin lo da de alta (POST /profesores): usuario = email, contraseña
// INICIAL = su DNI. Ver ProfesorService.
//
// El login pide SOLO email + contraseña. El idProfesor (idPersona) que el resto
// del portal necesita YA NO se piden a mano: apenas autenticamos, el front llama
// GET /profesores/me (rol PROFESOR; el email sale del token) y de ahi saca el
// idPersona, que se guarda en SesionProfesor. Ver ProfesorController.
// ============================================================

const formLogin = document.getElementById("form-login");

formLogin.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    ocultarMensaje();

    const datos = Object.fromEntries(new FormData(formLogin));

    // Validamos que no vengan campos vacíos
    if (!datos.email || !datos.password) {
        mostrarMensaje("Completa email y contraseña.");
        return;
    }

    try {
        // 1. Autenticación: Desestructuramos usando las llaves exactas del JSON de tu Postman
        const { acces_token, refresh_token } = await loginJwt(datos.email, datos.password);
        
        // 2. Guardamos la sesión (Se pasa 'acces_token' como 'token' y 'refresh_token' como 'refresh')
        Auth.guardarSesion({ 
            token: acces_token, 
            refresh: refresh_token, 
            rol: "PROFESOR" 
        });

        // 3. Obtenemos los datos del profesor logueado usando el token recién guardado
        const respuesta = await fetch(API_BASE + "/profesores/me");
        if (!respuesta.ok) {
            Auth.cerrar();
            throw new Error(await extraerMensajeError(respuesta));
        }
        
        const profesor = await respuesta.json();
        
        // 4. Guardamos el idPersona en la sesión del profesor y redirigimos
        SesionProfesor.guardar(profesor.idPersona);
        window.location.href = "/pages/Profesor/menu_profesor.html";

    } catch (error) {
        mostrarMensaje("No se pudo iniciar sesion: " + error.message);
    }
});