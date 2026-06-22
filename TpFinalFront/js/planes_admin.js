// ============================================================
// Gestion de planes de estudio (admin) - ABM completo
// Usa TODOS los endpoints de PlanDeEstudioController:
//   POST   /planDeEstudio/crear   -> crear (recibe la entidad: nombre, idCarrera, anioInicio)
//   GET    /planDeEstudio/mostrar -> listar
//   GET    /planDeEstudio/{id}    -> buscar por id (abre el editor)
//   PUT    /planDeEstudio/{id}    -> modificar (DTO: idCarrera, nombre)
//   DELETE /planDeEstudio/{id}    -> eliminar
//
// Nota: PlanDeEstudioDTO (mostrar y buscar por id) NO devuelve el id del plan
// ni el anioInicio, por eso el listado no muestra id y para editar/eliminar hay
// que buscar el plan por su id (que el admin ingresa a mano). Ver NOTAS_BACKEND.txt.
// ============================================================

const API_URL = "http://localhost:8080/planDeEstudio";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Agregar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-plan");
const formulario = document.getElementById("form-plan");

// Buscar por id / editar / eliminar
const inputBuscarId = document.getElementById("buscar-id");
const btnBuscarId = document.getElementById("btn-buscar-id");
const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");
const btnEliminar = document.getElementById("btn-eliminar");

let planEditandoId = null;

// ============================================================
// Listar (GET /planDeEstudio/mostrar)
// ============================================================
async function cargarPlanes() {
    try {
        const respuesta = await fetch(API_URL + "/mostrar");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const planes = await respuesta.json();
        mostrarPlanes(planes);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los planes: " + error.message);
    }
}

function mostrarPlanes(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" class="celda-vacia">No hay planes.</td></tr>`;
        return;
    }
    tbody.innerHTML = lista
        .map(
            (plan) => `
            <tr>
                <td>${plan.idPlan ?? "-"}</td> <td>${plan.idCarrera ?? "-"}</td>
                <td>${plan.nombre ?? ""}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Agregar (POST /planDeEstudio/crear) - recibe la entidad
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    const body = {
        nombre: datos.nombre,
        idCarrera: datos.idCarrera === "" ? null : Number(datos.idCarrera),
        anioInicio: datos.anioInicio === "" ? null : Number(datos.anioInicio),
    };

    try {
        const respuesta = await fetch(API_URL + "/crear", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargarPlanes();
    } catch (error) {
        mostrarMensaje("No se pudo guardar el plan: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Buscar por id (GET /planDeEstudio/{id}) -> abre el editor
// ============================================================
btnBuscarId.addEventListener("click", buscarPorId);
inputBuscarId.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") buscarPorId();
});

async function buscarPorId() {
    const id = inputBuscarId.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del plan.");
        return;
    }

    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const plan = await respuesta.json();
        abrirEditor(id, plan);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se encontro el plan: " + error.message);
    }
}

function abrirEditor(id, plan) {
    planEditandoId = id;
    editarIdSpan.textContent = "#" + id;

    formEditar.elements.nombre.value = plan.nombre ?? "";
    formEditar.elements.idCarrera.value = plan.idCarrera ?? "";

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

// ============================================================
// Modificar (PUT /planDeEstudio/{id}) - recibe el DTO (idCarrera, nombre)
// ============================================================
formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formEditar));

    const body = {
        nombre: datos.nombre,
        idCarrera: datos.idCarrera === "" ? null : Number(datos.idCarrera),
    };

    try {
        const respuesta = await fetch(API_URL + "/" + planEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarPlanes();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /planDeEstudio/{id})
// ============================================================
btnEliminar.addEventListener("click", async () => {
    const confirmado = confirm("¿Eliminar el plan #" + planEditandoId + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + planEditandoId, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarPlanes();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el plan: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Utilidades
// ============================================================
async function extraerMensajeError(respuesta) {
    try {
        const cuerpo = await respuesta.json();
        return cuerpo.mensaje || cuerpo.message || cuerpo.error || ("Error " + respuesta.status);
    } catch {
        return "Error " + respuesta.status;
    }
}

function mostrarMensaje(texto) {
    mensaje.textContent = texto;
    mensaje.hidden = false;
}

function ocultarMensaje() {
    mensaje.hidden = true;
}

// ============================================================
// Arranque
// ============================================================
cargarPlanes();
