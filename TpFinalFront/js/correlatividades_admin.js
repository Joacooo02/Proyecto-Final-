// ============================================================
// Gestion de correlatividades (admin)
// Usa TODOS los endpoints de CorrelatividadController:
//   POST   /correlatividades                 -> crear
//   GET    /correlatividades                 -> listar
//   GET    /correlatividades/materia/{idMateria} -> filtrar por materia
//   DELETE /correlatividades/{id}            -> eliminar
//   PUT    /correlatividades/{id}            -> modificar
// ============================================================

const API_URL = "http://localhost:8080/correlatividades";
const API_MATERIAS = "http://localhost:8080/materias";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

const filtroMateria = document.getElementById("filtro-materia");
const btnBuscar = document.getElementById("btn-buscar");
const btnVerTodas = document.getElementById("btn-ver-todas");

const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-correlatividad");
const formulario = document.getElementById("form-correlatividad");

// Mapa idMateria -> nombre, para mostrar nombres en la tabla.
let materiasMap = {};
let correlatividadEdicionId = null; // Guarda el ID si estamos editando

// ============================================================
// Cargar materias (para los desplegables y el mapa de nombres)
// ============================================================
async function cargarMaterias() {
    try {
        const respuesta = await fetch(API_MATERIAS);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const materias = await respuesta.json();

        const selects = [
            formulario.elements.idMateria,
            formulario.elements.idMateriaCorrelativa,
            filtroMateria,
        ];

        for (const materia of materias) {
            materiasMap[materia.id] = materia.nombre;
            for (const select of selects) {
                const opcion = document.createElement("option");
                opcion.value = materia.id;
                opcion.textContent = materia.nombre;
                select.appendChild(opcion);
            }
        }
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las materias: " + error.message);
    }
}

function nombreMateria(id) {
    return materiasMap[id] ? `${materiasMap[id]} (#${id})` : "#" + id;
}

// ============================================================
// Listar (GET /correlatividades  y  GET /correlatividades/materia/{id})
// ============================================================
async function cargarCorrelatividades(url = API_URL) {
    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const correlatividades = await respuesta.json();
        mostrarCorrelatividades(correlatividades);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las correlatividades: " + error.message);
    }
}

function mostrarCorrelatividades(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="celda-vacia">No hay correlatividades.</td></tr>`;
        return;
    }

    for (const c of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${c.idCorrelatividad}</td>
            <td>${nombreMateria(c.idMateria)}</td>
            <td>${nombreMateria(c.idMateriaCorrelativa)}</td>
            <td>${c.estadoParaCursar ?? "-"}</td>
            <td>${c.estadoParaRendir ?? "-"}</td>
            <td>
                <button type="button" class="btn btn-editar">Editar</button>
                <button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button>
            </td>
        `;
        
        fila.querySelector(".btn-editar").addEventListener("click", () =>
            prepararEditar(c)
        );
        fila.querySelector(".btn-eliminar").addEventListener("click", () =>
            eliminarCorrelatividad(c.idCorrelatividad)
        );
        tbody.appendChild(fila);
    }
}

// ============================================================
// Filtros (Corregido con la barra "/" necesaria)
// ============================================================
btnBuscar.addEventListener("click", () => {
    const id = filtroMateria.value;
    if (id === "") {
        cargarCorrelatividades(API_URL);
    } else {
        cargarCorrelatividades(API_URL + "/materia/" + id);
    }
});

btnVerTodas.addEventListener("click", () => {
    filtroMateria.value = "";
    cargarCorrelatividades(API_URL);
});

// ============================================================
// Agregar / Editar Acciones del Modal
// ============================================================
btnAgregar.addEventListener("click", () => {
    correlatividadEdicionId = null;
    formulario.reset();
    dialogo.querySelector("h2").textContent = "Agregar correlatividad";
    dialogo.showModal();
});

btnCancelar.addEventListener("click", () => dialogo.close());

function prepararEditar(c) {
    correlatividadEdicionId = c.idCorrelatividad;
    dialogo.querySelector("h2").textContent = "Editar correlatividad";

    formulario.elements.idMateria.value = c.idMateria;
    formulario.elements.idMateriaCorrelativa.value = c.idMateriaCorrelativa;
    formulario.elements.estadoParaCursar.value = c.estadoParaCursar || "";
    formulario.elements.estadoParaRendir.value = c.estadoParaRendir || "";

    dialogo.showModal();
}

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    const body = {
        idMateria: Number(datos.idMateria),
        idMateriaCorrelativa: Number(datos.idMateriaCorrelativa),
        estadoParaCursar: datos.estadoParaCursar || null,
        estadoParaRendir: datos.estadoParaRendir || null,
    };

    try {
        let url = API_URL;
        let metodo = "POST";

        if (correlatividadEdicionId) {
            url = `${API_URL}/${correlatividadEdicionId}`;
            metodo = "PUT";
        }

        const respuesta = await fetch(url, {
            method: metodo,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        correlatividadEdicionId = null;
        cargarCorrelatividades();
    } catch (error) {
        mostrarMensaje("No se pudo guardar la correlatividad: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Eliminar (DELETE /correlatividades/{id})
// ============================================================
async function eliminarCorrelatividad(id) {
    const confirmado = confirm("¿Eliminar la correlatividad #" + id + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarCorrelatividades();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la correlatividad: " + error.message);
    }
}

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
// Arranque: primero las materias (para los nombres), despues la lista.
// ============================================================
async function init() {
    await cargarMaterias();
    cargarCorrelatividades();
}
init();