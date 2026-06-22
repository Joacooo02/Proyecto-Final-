// ============================================================
// Gestion de inscripciones (admin)
// ============================================================

const BASE_URL = "http://localhost:8080";
const URL_PERIODOS = BASE_URL + "/periodo/inscripcion"; 
const URL_PERIODOS_COMISIONES = BASE_URL + "/periodos"; 

let listaPeriodosOriginal = [];
let listaComisionesOriginal = [];

// --- Referencias del HTML ---
const mensaje = document.getElementById("mensaje");

// Periodos
const tbodyPeriodos = document.getElementById("tbody-periodos");
const btnAgregarPeriodo = document.getElementById("btn-agregar-periodo");
const dialogoPeriodo = document.getElementById("dialogo-periodo");
const formPeriodo = document.getElementById("form-periodo");
const btnCancelarPeriodo = document.getElementById("btn-cancelar-periodo");
const buscarPeriodoInput = document.getElementById("buscar-periodo");

// Comisiones habilitadas
const tbodyComisiones = document.getElementById("tbody-comisiones");
const inputPeriodo = document.getElementById("input-periodo");
const btnVerComisiones = document.getElementById("btn-ver-comisiones");
const btnHabilitarComision = document.getElementById("btn-habilitar-comision");
const dialogoComision = document.getElementById("dialogo-comision");
const formComision = document.getElementById("form-comision");
const btnCancelarComision = document.getElementById("btn-cancelar-comision");
const buscarComisionInput = document.getElementById("buscar-comision");

// ============================================================
// Periodos de inscripcion
// ============================================================

async function cargarPeriodos() {
    try {
        const respuesta = await fetch(URL_PERIODOS + "/mostrar");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        listaPeriodosOriginal = await respuesta.json(); 
        mostrarPeriodos(listaPeriodosOriginal);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los periodos: " + error.message);
    }
}

function mostrarPeriodos(lista) {
    tbodyPeriodos.innerHTML = "";

    for (const periodo of lista) {
        // NOTA: Asegurate si tu DTO usa 'id' o 'idPeriodo'. Asumimos 'id' por tu backend.
        const idActual = periodo.id; 

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${periodo.idCarrera ?? ""}</td>
            <td>${periodo.tipoInscripcion ?? ""}</td>
            <td>${periodo.anioLectivo ?? ""}</td>
            <td>${periodo.cuatrimestre ?? ""}</td>
            <td>${formatearFecha(periodo.fechaInicio)}</td>
            <td>${formatearFecha(periodo.fechaCierre)}</td>
            <td>${periodo.activa ? "Si" : "No"}</td>
            <td>
                <div style="display: flex; gap: 4px;">
                    <button class="btn" style="background-color: #f0ad4e; color: white; padding: 2px 6px;" 
                        onclick="alternarEstadoPeriodo(${idActual}, ${periodo.activa})">
                        ${periodo.activa ? "Desactivar" : "Activar"}
                    </button>
                    <button class="btn" style="background-color: #d9534f; color: white; padding: 2px 6px;" 
                        onclick="eliminarPeriodo(${idActual})">Eliminar</button>
                </div>
            </td>
        `;
        tbodyPeriodos.appendChild(fila);
    }
}

// NUEVA FUNCIÓN: ELIMINAR PERIODO (Usa tu @DeleteMapping("/{id}"))
async function eliminarPeriodo(id) {
    if (!id) {
        mostrarMensaje("Error: El DTO del backend no está enviando el campo ID del período.");
        return;
    }
    if (!confirm(`¿Estás seguro de eliminar el período ID: ${id}?`)) return;

    try {
        const respuesta = await fetch(`${URL_PERIODOS}/${id}`, {
            method: "DELETE"
        });

        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        
        ocultarMensaje();
        cargarPeriodos(); // Recarga la tabla
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el periodo: " + error.message);
    }
}

// NUEVA FUNCIÓN: MODIFICAR PERIODO (Usa tu @PutMapping("/{id}"))
// Cambia rápidamente el estado Activa (Si/No) sin necesidad de abrir todo un formulario complejo
async function alternarEstadoPeriodo(id, estadoActual) {
    if (!id) return;

    // Buscamos el objeto original para no romper los otros campos obligatorios que pide el DTO
    const periodoOriginal = listaPeriodosOriginal.find(p => p.id === id);
    if (!periodoOriginal) return;

    // Clonamos y modificamos sólo el estado 'activa'
    const periodoModificado = { ...periodoOriginal, activa: !estadoActual };

    try {
        const respuesta = await fetch(`${URL_PERIODOS}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(periodoModificado)
        });

        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));

        ocultarMensaje();
        cargarPeriodos();
    } catch (error) {
        mostrarMensaje("No se pudo modificar el periodo: " + error.message);
    }
}

// Exponer funciones al entorno global para los onclicks
window.eliminarPeriodo = eliminarPeriodo;
window.alternarEstadoPeriodo = alternarEstadoPeriodo;

// Filtro de periodos en tiempo real
buscarPeriodoInput.addEventListener("input", () => {
    const texto = buscarPeriodoInput.value.toLowerCase().trim();
    const filtrados = listaPeriodosOriginal.filter(p => 
        (p.idCarrera && p.idCarrera.toString().includes(texto)) ||
        (p.tipoInscripcion && p.tipoInscripcion.toLowerCase().includes(texto)) ||
        (p.anioLectivo && p.anioLectivo.toString().includes(texto))
    );
    mostrarPeriodos(filtrados);
});

btnAgregarPeriodo.addEventListener("click", () => dialogoPeriodo.showModal());
btnCancelarPeriodo.addEventListener("click", () => dialogoPeriodo.close());

formPeriodo.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formPeriodo));
    datos.activa = formPeriodo.elements.activa.checked;

    if (datos.cuatrimestre === "") {
        datos.cuatrimestre = null;
    }

    try {
        const respuesta = await fetch(URL_PERIODOS + "/crear", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoPeriodo.close();
        formPeriodo.reset();
        cargarPeriodos();
    } catch (error) {
        mostrarMensaje("No se pudo crear el periodo: " + error.message);
        dialogoPeriodo.close();
    }
});

// ============================================================
// Comisiones habilitadas en un periodo
// ============================================================

async function verComisiones() {
    const idPeriodo = inputPeriodo.value.trim();
    if (idPeriodo === "") {
        mostrarMensaje("Ingresa el id del periodo para ver sus comisiones.");
        return;
    }

    try {
        const respuesta = await fetch(URL_PERIODOS_COMISIONES + "/" + idPeriodo + "/comisiones");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        listaComisionesOriginal = await respuesta.json(); 
        mostrarComisiones(listaComisionesOriginal);
        
        buscarComisionInput.style.display = listaComisionesOriginal.length > 0 ? "inline-block" : "none";
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las comisiones: " + error.message);
    }
}

function mostrarComisiones(lista) {
    tbodyComisiones.innerHTML = "";

    for (const comision of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${comision.id ?? ""}</td>
            <td>${comision.idPeriodo ?? ""}</td>
            <td>${comision.idComision ?? ""}</td>
            <td>${comision.materia ?? ""}</td>
            <td>${comision.nroComision ?? ""}</td>
            <td>-</td> 
        `;
        tbodyComisiones.appendChild(fila);
    }
}

buscarComisionInput.addEventListener("input", () => {
    const texto = buscarComisionInput.value.toLowerCase().trim();
    const filtrados = listaComisionesOriginal.filter(c => 
        (c.materia && c.materia.toLowerCase().includes(texto)) ||
        (c.nroComision && c.nroComision.toString().includes(texto))
    );
    mostrarComisiones(filtrados);
});

btnVerComisiones.addEventListener("click", verComisiones);
inputPeriodo.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") {
        verComisiones();
    }
});

btnHabilitarComision.addEventListener("click", () => {
    formComision.elements.idPeriodo.value = inputPeriodo.value.trim();
    dialogoComision.showModal();
});
btnCancelarComision.addEventListener("click", () => dialogoComision.close());

formComision.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const idPeriodo = formComision.elements.idPeriodo.value.trim();
    const idComision = formComision.elements.idComision.value.trim();

    try {
        const respuesta = await fetch(
            URL_PERIODOS_COMISIONES + "/" + idPeriodo + "/comisiones/" + idComision,
            { method: "POST" }
        );
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoComision.close();
        formComision.reset();
        inputPeriodo.value = idPeriodo;
        verComisiones();
    } catch (error) {
        mostrarMensaje("No se pudo habilitar la comision: " + error.message);
        dialogoComision.close();
    }
});

// ============================================================
// Utilidades
// ============================================================

function formatearFecha(valor) {
    if (!valor) return "";
    const fecha = new Date(valor);
    if (isNaN(fecha.getTime())) return valor; 
    return fecha.toLocaleDateString("es-AR");
}

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

cargarPeriodos();