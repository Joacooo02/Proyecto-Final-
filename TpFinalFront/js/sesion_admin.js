// ============================================================
// Sesion del administrador (compartida por todas las paginas del admin)
// ------------------------------------------------------------
// El admin NO tenia archivo de sesion porque antes la seguridad estaba
// desactivada. Ahora el backend pide JWT, asi que cada pagina del admin:
//   - exige que haya un token (sino, vuelve al login),
//   - engancha el boton "Salir" de la navbar para cerrar la sesion.
//
// El token se obtiene en login_admin.js (POST /auth/login) y lo maneja Auth
// (definido en api.js). El admin no necesita ids extra (legajo/idProfesor):
// trabaja por legajo/id que ya vienen en cada listado.
//
// IMPORTANTE: para tener un usuario ADMIN hay que crearlo en la base, porque
// POST /auth/register RECHAZA el rol ADMIN (AuthService). Ver NOTAS_BACKEND.txt.
// ============================================================

const SesionAdmin = {
    // Se llama al inicio de cada pagina del admin.
    requerir() {
        if (!Auth.haySesion()) {
            window.location.href = "/pages/Administrador/login_admin.html";
        }
    },
    cerrar() {
        Auth.cerrar();
    },
};

// Boton "Salir" de la navbar del admin (navbar_admin.js lo inyecta con id="nav-salir").
document.getElementById("nav-salir")?.addEventListener("click", (evento) => {
    evento.preventDefault();
    SesionAdmin.cerrar();
    window.location.href = "/pages/Administrador/login_admin.html";
});

// Todas las paginas del admin requieren sesion (el login no carga este archivo).
SesionAdmin.requerir();
