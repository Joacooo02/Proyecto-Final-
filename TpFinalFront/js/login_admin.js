// ============================================================
// Login del administrador  (JWT real)
// ------------------------------------------------------------
// POST /auth/login con { email, password }. Si anda, guarda el token y el rol
// y entra al panel. El backend valida credenciales y, en cada endpoint, el rol.
//
// OJO: para que exista un usuario ADMIN hay que crearlo a mano en la base, porque
// POST /auth/register RECHAZA el rol ADMIN (AuthService.register). El alumno y el
// profesor, en cambio, se crean desde el alta (POST /alumnos / POST /profesores),
// que ademas les genera el usuario de login. Ver NOTAS_BACKEND.txt.
// ============================================================

const formLogin = document.getElementById("form-login");

formLogin.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    ocultarMensaje();

    const datos = Object.fromEntries(new FormData(formLogin));

    // CORREGIDO: Ahora usa datos.password para coincidir con el HTML modificado
    if (!datos.email || !datos.password) {
        mostrarMensaje("Completa email y contraseña.");
        return;
    }

    try {
        // CORREGIDO: Desestructuración con las llaves correctas de api.js
        const { acces_token, refresh_token } = await loginJwt(datos.email, datos.password);
        Auth.guardarSesion({ token: acces_token, refresh: refresh_token, rol: "ADMIN" });
        window.location.href = "/pages/Administrador/menu_admin.html";
    } catch (error) {
        mostrarMensaje("No se pudo iniciar sesion: " + error.message);
    }
});