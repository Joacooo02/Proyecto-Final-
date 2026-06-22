// ============================================================
// Gestion de usuarios (admin) - Solo Visualización y Búsqueda
// ============================================================

const API_USUARIOS = "http://localhost:8080/usuarios";

const mensaje = document.getElementById("mensaje");
const tbody = document.getElementById("tbody-usuarios");
const buscador = document.getElementById("buscador-usuario");

let listaUsuariosGlobal = []; 

// ============================================================
// Listar usuarios (GET /usuarios)
// ============================================================
async function cargarUsuarios() {
    try {
        console.log("Iniciando carga de usuarios desde:", API_USUARIOS);
        const respuesta = await fetch(API_USUARIOS);
        if (!respuesta.ok) {
            throw new Error("Error " + respuesta.status);
        }
        listaUsuariosGlobal = await respuesta.json();
        console.log("Usuarios cargados con éxito:", listaUsuariosGlobal);
        mostrarUsuarios(listaUsuariosGlobal);
    } catch (error) {
        console.error("Error al cargar:", error);
        mostrarMensaje("No se pudieron cargar los usuarios: " + error.message);
    }
}

function mostrarUsuarios(lista) {
    if (!tbody) {
        console.error("Error: No se encontró el elemento #tbody-usuarios en el HTML.");
        return;
    }

    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="2" class="celda-vacia" style="text-align:center; padding: 20px;">No se encontraron usuarios.</td></tr>`;
        return;
    }

    tbody.innerHTML = lista
        .map(
            (u) => `
            <tr>
                <td>${u.username ?? "-"}</td>
                <td>${u.email ?? u.password ?? "-"}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Lógica del Buscador (Solo si el elemento existe en el HTML)
// ============================================================
if (buscador) {
    buscador.addEventListener("input", (evento) => {
        const textoBusqueda = evento.target.value.toLowerCase();

        const usuariosFiltrados = listaUsuariosGlobal.filter((u) => {
            const username = (u.username ?? "").toLowerCase();
            const email = (u.email ?? u.password ?? "").toLowerCase();
            
            return username.includes(textoBusqueda) || email.includes(textoBusqueda);
        });

        mostrarUsuarios(usuariosFiltrados);
    });
} else {
    console.warn("Advertencia: No se encontró el input #buscador-usuario en el HTML.");
}

function mostrarMensaje(texto, tipo = "error") {
    if (mensaje) {
        mensaje.textContent = texto;
        mensaje.classList.toggle("mensaje-ok", tipo === "ok");
        mensaje.hidden = false;
    }
}

// ============================================================
// Arranque
// ============================================================
cargarUsuarios();