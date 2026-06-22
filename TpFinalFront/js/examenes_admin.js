// ============================================================
// Gestion de examenes (admin)
// Usa TODOS los endpoints de ExamenController:
//   POST /examenes/materia/{idMateria}     -> agregar (la materia va por la URL)
//   GET  /examenes                         -> listar todos
//   GET  /examenes/{idExamen}              -> ver uno por id
//   GET  /examenes/tipo/{tipoExamen}       -> filtrar por tipo (PARCIAL/FINAL)
//   GET  /examenes/materia/{nombreMateria} -> filtrar por nombre de materia
//   GET  /examenes/fecha/{fecha}           -> filtrar por fecha (YYYY-MM-DD)
//   DELETE /examenes/{id}                  -> eliminar examen
//   PUT   /examenes/{id}                   -> modificar examen
// ============================================================

const API_URL = "http://localhost:8080/examenes";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Filtros
const modoFiltro = document.getElementById("modo-filtro");
const btnBuscar = document.getElementById("btn-buscar");
const valorTipo = document.getElementById("valor-tipo");
const valorMateria = document.getElementById("valor-materia");
const valorFecha = document.getElementById("valor-fecha");
const valorId = document.getElementById("valor-id");

// Agregar / Editar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-examen");
const formulario = document.getElementById("form-examen");

let examenEdicionId = null; // Guarda el ID si estamos editando

// ============================================================
// Mostrar el control de filtro segun el modo elegido
// ============================================================
modoFiltro.addEventListener("change", () => {
    // Oculta todos y muestra solo el que corresponde.
    for (const control of document.querySelectorAll(".filtro-control")) {
        control.hidden = true;
    }
    const mapa = { tipo: valorTipo, materia: valorMateria, fecha: valorFecha, id: valorId };
    if (mapa[modoFiltro.value]) {
        mapa[modoFiltro.value].hidden = false;
    }
});

// ============================================================
// Buscar (arma la URL segun el modo)
// ============================================================
btnBuscar.addEventListener("click", buscar);

async function buscar() {
    let url = API_URL;
    let unico = false; // "por id" devuelve un solo examen, no una lista

    switch (modoFiltro.value) {
        case "tipo":
            url = API_URL + "/tipo/" + valorTipo.value;
            break;
        case "materia":
            if (valorMateria.value.trim() === "") {
                mostrarMensaje("Ingresa el nombre de la materia.");
                return;
            }
            url = API_URL + "/materia/" + encodeURIComponent(valorMateria.value.trim());
            break;
        case "fecha":
            if (valorFecha.value === "") {
                mostrarMensaje("Elegi una fecha.");
                return;
            }
            url = API_URL + "/fecha/" + valorFecha.value; // input date -> YYYY-MM-DD
            break;
        case "id":
            if (valorId.value.trim() === "") {
                mostrarMensaje("Ingresa el id del examen.");
                return;
            }
            url = API_URL + "/" + valorId.value.trim();
            unico = true;
            break;
        default:
            url = API_URL; // todos
    }

    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const datos = await respuesta.json();
        // "por id" devuelve un objeto; el resto, un array. Unificamos a lista.
        mostrarExamenes(unico ? [datos] : datos);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los examenes: " + error.message);
    }
}

function mostrarExamenes(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="celda-vacia">No hay examenes.</td></tr>`;
        return;
    }
    tbody.innerHTML = lista
        .map(
            (examen) => `
            <tr>
                <td>${examen.idExamen}</td>
                <td>${examen.materia ? examen.materia.nombre : "-"}</td>
                <td>${formatearFecha(examen.fecha)}</td>
                <td>${examen.tipoExamen ?? "-"}</td>
                <td>
                    <button class="btn btn-editar" onclick="prepararEditar(${examen.idExamen}, ${examen.materia ? examen.materia.idMateria : null}, '${examen.fecha}', '${examen.tipoExamen}')">Editar</button>
                    <button class="btn btn-eliminar" onclick="confirmarEliminar(${examen.idExamen})">Eliminar</button>
                </td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Agregar / Editar Acciones del Modal
// ============================================================
btnAgregar.addEventListener("click", () => {
    examenEdicionId = null;
    formulario.reset();
    dialogo.querySelector("h2").textContent = "Agregar examen";
    formulario.querySelector("input[name='idMateria']").disabled = false;
    dialogo.showModal();
});

btnCancelar.addEventListener("click", () => dialogo.close());

window.confirmarEliminar = async function(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este examen?")) {
        try {
            const respuesta = await fetch(`${API_URL}/${id}`, {
                method: "DELETE"
            });
            if (!respuesta.ok) {
                throw new Error(await extraerMensajeError(respuesta));
            }
            buscar();
        } catch (error) {
            mostrarMensaje("No se pudo eliminar el examen: " + error.message);
        }
    }
}

window.prepararEditar = function(id, idMateria, fecha, tipoExamen) {
    examenEdicionId = id;
    dialogo.querySelector("h2").textContent = "Editar examen";
    
    formulario.querySelector("input[name='idMateria']").value = idMateria || "";
    formulario.querySelector("input[name='idMateria']").disabled = true;
    formulario.querySelector("input[name='fecha']").value = fecha || "";
    formulario.querySelector("select[name='tipoExamen']").value = tipoExamen || "PARCIAL";
    
    dialogo.showModal();
}

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    
    const inputMateria = formulario.querySelector("input[name='idMateria']");
    inputMateria.disabled = false; 
    const datos = Object.fromEntries(new FormData(formulario));
    if (examenEdicionId) inputMateria.disabled = true; 

    const idMateria = datos.idMateria;
    const body = {
        fecha: datos.fecha === "" ? null : datos.fecha,
        tipoExamen: datos.tipoExamen,
    };

    try {
        let url;
        let metodo;

        if (examenEdicionId) {
            url = `${API_URL}/${examenEdicionId}?idMateria=${idMateria}`;
            metodo = "PUT";
        } else {
            url = API_URL + "/materia/" + idMateria;
            metodo = "POST";
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
        examenEdicionId = null;
        buscar(); 
    } catch (error) {
        mostrarMensaje("No se pudo guardar el examen: " + error.message);
        dialogo.close();
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

// ============================================================
// Arranque: traemos todos los examenes.
// ============================================================
buscar();