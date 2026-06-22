// ============================================================
// api.js  —  Capa compartida de TODOS los portales (admin / alumno / profesor)
// ------------------------------------------------------------
// Se carga PRIMERO en cada pagina (antes que navbar / sesion / modulos).
//
// El backend AHORA tiene seguridad JWT activada (Spring Security + @PreAuthorize
// por rol). Antes estaba en permitAll() y el front iba sin autenticarse; por eso
// este archivo es nuevo. Hace 3 cosas:
//
//   1) Define API_BASE (URL del backend Spring).
//   2) Maneja el token JWT: login real contra POST /auth/login y lo guarda en
//      sessionStorage (ver Auth + loginJwt).
//   3) Parchea window.fetch para que CADA pedido al backend mande, solo, el header
//         Authorization: Bearer <token>
//      sin tener que tocar el fetch de cada modulo. Si el backend responde 401
//      (token ausente/vencido) limpia la sesion y manda al login del portal.
//
// Ademas centraliza los helpers que antes estaban duplicados en cada archivo:
// extraerMensajeError, mostrarMensaje, ocultarMensaje, formatearFecha, siNo.
// ============================================================

// Base de la API de Spring. El backend corre en :8080 (application.yml) sin
// context-path, asi que los endpoints son http://localhost:8080/<lo-que-sea>.
const API_BASE = "http://localhost:8080";

// ------------------------------------------------------------
// Manejo del token JWT
// ------------------------------------------------------------
// Guardamos el access token, el refresh token y el ROL elegido en el login.
// (El backend NO devuelve el rol en el login ni lo mete en el JWT, asi que el
//  rol que guardamos es el del portal por el que entro el usuario. Las acciones
//  reales las sigue validando el backend por rol -> si no corresponde, da 403.)
const Auth = {
    guardarSesion({ token, refresh, rol } = {}) {
        if (token) sessionStorage.setItem("auth_token", token);
        if (refresh) sessionStorage.setItem("auth_refresh", refresh);
        if (rol) sessionStorage.setItem("auth_rol", rol);
    },
    token() { return sessionStorage.getItem("auth_token"); },
    refresh() { return sessionStorage.getItem("auth_refresh"); },
    rol() { return sessionStorage.getItem("auth_rol"); },
    haySesion() { return !!this.token(); },
    cerrar() {
        sessionStorage.removeItem("auth_token");
        sessionStorage.removeItem("auth_refresh");
        sessionStorage.removeItem("auth_rol");
    },
};

// Login real contra el backend.
//   POST /auth/login   body { email, password }
//   respuesta          { acces_token, refresh_token }
async function loginJwt(email, contrasena) {
    const respuesta = await fetch(API_BASE + "/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // CORREGIDO: Se envía "password" en vez de "contrasena" para que Spring lo entienda
        body: JSON.stringify({ email, password: contrasena }),
    });

    if (!respuesta.ok) {
        throw new Error(await extraerMensajeError(respuesta));
    }

    const data = await respuesta.json();
    const token = data.acces_token || data.access_token || data.token;
    const refresh = data.refresh_token || data.refreshToken;

    if (!token) {
        throw new Error("El servidor no devolvio un token de acceso.");
    }
    
    // CORREGIDO: Retornamos las propiedades mapeadas con los nombres exactos que espera el login del profesor
    return { acces_token: token, refresh_token: refresh };
}

// ------------------------------------------------------------
// A que login volver segun en que portal estes parado.
// ------------------------------------------------------------
function rutaLoginActual() {
    const ruta = window.location.pathname;
    if (ruta.includes("/Alumno/")) return "/pages/Alumno/login_alumno.html";
    if (ruta.includes("/Profesor/")) return "/pages/Profesor/login_profesor.html";
    if (ruta.includes("/Administrador/")) return "/pages/Administrador/login_admin.html";
    return "/pages/index.html";
}

// ------------------------------------------------------------
// Parche de window.fetch:
//   - A todo pedido al backend (menos /auth/**) le agrega el Bearer token.
//   - Si el backend responde 401, cierra sesion y manda al login.
// Asi NINGUN modulo necesita preocuparse por el header de autorizacion.
// ------------------------------------------------------------
(function parchearFetch() {
    const fetchOriginal = window.fetch.bind(window);

    window.fetch = function (recurso, opciones = {}) {
        const url = typeof recurso === "string" ? recurso : (recurso && recurso.url);
        const esBackend = typeof url === "string" && url.startsWith(API_BASE);
        const esAuth = esBackend && url.includes("/auth/");

        if (esBackend && !esAuth) {
            const token = Auth.token();
            const headers = new Headers(opciones.headers || {});
            if (token && !headers.has("Authorization")) {
                headers.set("Authorization", "Bearer " + token);
            }
            opciones = { ...opciones, headers };
        }

        return fetchOriginal(recurso, opciones).then((respuesta) => {
            if (respuesta.status === 401 && esBackend && !esAuth) {
                Auth.cerrar();
                if (!window.location.pathname.includes("login")) {
                    window.location.href = rutaLoginActual();
                }
            }
            return respuesta;
        });
    };
})();

// ------------------------------------------------------------
// Helpers compartidos (antes copiados en cada archivo)
// ------------------------------------------------------------

// Lee el mensaje de error del GlobalExceptionHandler del backend
// (ErrorResponse { timestamp, status, error, message }).
async function extraerMensajeError(respuesta) {
    try {
        // Clonamos la respuesta para poder leerla como texto si el JSON falla
        const respuestaClonada = respuesta.clone();
        try {
            const cuerpo = await respuesta.json();
            return cuerpo.mensaje || cuerpo.message || cuerpo.error || ("Error " + respuesta.status);
        } catch (jsonError) {
            // Si no era un JSON válido, leemos el texto puro que mandó el servidor
            const textoPlano = await respuestaClonada.text();
            // Si el texto es muy largo o raro, devolvemos solo el código de estado
            if (textoPlano && textoPlano.length < 100) return textoPlano;
            return "Error del servidor (" + respuesta.status + ")";
        }
    } catch (e) {
        if (respuesta.status === 403) return "No tenes permiso para esta accion (rol incorrecto).";
        return "Error " + respuesta.status;
    }
}

function mostrarMensaje(texto, tipo = "error") {
    const mensaje = document.getElementById("mensaje");
    if (!mensaje) return;
    mensaje.textContent = texto;
    mensaje.classList.toggle("mensaje-ok", tipo === "ok");
    mensaje.hidden = false;
}

function ocultarMensaje() {
    const mensaje = document.getElementById("mensaje");
    if (mensaje) mensaje.hidden = true;
}

// Muestra una fecha legible; si viene null/invalida deja el valor o vacio.
function formatearFecha(valor) {
    if (!valor) return "";
    const fecha = new Date(valor);
    if (isNaN(fecha.getTime())) return valor;
    return fecha.toLocaleDateString("es-AR");
}

function siNo(valor) {
    return valor ? "Si" : "No";
}