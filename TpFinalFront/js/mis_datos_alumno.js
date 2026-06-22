// ============================================================
// Mis datos (alumno)
// Muestra el perfil, el historial academico, las materias inscriptas
// y el plan de estudio del alumno logueado.
// ============================================================

SesionAlumno.requerir();

const legajo = SesionAlumno.legajo();
const idAlumno = SesionAlumno.idAlumno();
// Helper para obtener el token
const getAuthHeaders = () => ({ 'Authorization': 'Bearer ' + sessionStorage.getItem('auth_token') });

const fichaPerfil = document.getElementById("ficha-perfil");
const tbodyHistorial = document.getElementById("tbody-historial");
const tbodyMaterias = document.getElementById("tbody-materias");
const tbodyPlan = document.getElementById("tbody-plan");

// ============================================================
// Perfil
// ============================================================
async function cargarPerfil() {
    try {
        const respuesta = await fetch(API_BASE + "/alumnos/" + legajo, { headers: getAuthHeaders() });
        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        const alumno = await respuesta.json();
        mostrarPerfil(alumno);
    } catch (error) {
        fichaPerfil.innerHTML = `<p class="celda-vacia">No se pudo cargar tu perfil (${error.message}).</p>`;
    }
}

function mostrarPerfil(alumno) {
    const datos = [
        ["Legajo", alumno.legajo],
        ["Nombre", alumno.nombre],
        ["Apellido", alumno.apellido],
        ["Dni", alumno.dni],
        ["Telefono", alumno.telefono ?? "-"],
        ["Email", alumno.email ?? "-"],
        ["Fecha de nacimiento", formatearFecha(alumno.fechaNacimiento) || "-"],
        ["Año de ingreso", alumno.anioIngreso ?? "-"],
        ["Promedio", alumno.promedio ?? "-"],
        ["Regular", siNo(alumno.esRegular)],
        ["Analitico parcial", siNo(alumno.analiticoParcial)],
    ];

    fichaPerfil.innerHTML = datos
        .map(([etiqueta, valor]) => `
            <div>
                <span class="ficha-label">${etiqueta}</span>
                <span class="ficha-valor">${valor}</span>
            </div>`)
        .join("");
}

// ============================================================
// Historial academico
// ============================================================
async function cargarHistorial() {
    try {
        const respuesta = await fetch(API_BASE + "/alumnos/" + legajo + "/historial-academico", { headers: getAuthHeaders() });
        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        const historial = await respuesta.json();
        mostrarHistorial(historial);
    } catch (error) {
        mostrarMensaje("No se pudo cargar el historial: " + error.message);
    }
}

function mostrarHistorial(lista) {
    if (!lista || lista.length === 0) {
        filaVacia(tbodyHistorial, 5, "Sin registros en el historial.");
        return;
    }
    tbodyHistorial.innerHTML = lista
        .map((item) => `
            <tr>
                <td>${item.materia ?? ""}</td>
                <td>${item.tipoExamen ?? ""}</td>
                <td>${item.nota ?? "-"}</td>
                <td>${formatearFecha(item.fechaExamen)}</td>
                <td>${item.estadoMateria ?? ""}</td>
            </tr>`)
        .join("");
}

// ============================================================
// Materias inscriptas y plan de estudio
// ============================================================
async function cargarMateriasInscriptas() {
    try {
        const respuesta = await fetch(API_BASE + "/alumnos/" + legajo + "/materias", { headers: getAuthHeaders() });
        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        const materias = await respuesta.json();
        mostrarMaterias(tbodyMaterias, materias, "No estas inscripto en ninguna materia.");
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las materias: " + error.message);
    }
}

async function cargarPlan() {
    try {
        const respuesta = await fetch(API_BASE + "/materias/plan-academico/" + idAlumno, { headers: getAuthHeaders() });
        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        const plan = await respuesta.json();
        mostrarMaterias(tbodyPlan, plan, "No hay materias en el plan.");
    } catch (error) {
        filaVacia(tbodyPlan, 4, "Plan no disponible.");
    }
}

function mostrarMaterias(tbody, lista, textoVacio) {
    if (!lista || lista.length === 0) {
        filaVacia(tbody, 4, textoVacio);
        return;
    }
    tbody.innerHTML = lista
        .map((materia) => `
            <tr>
                <td>${materia.nombre ?? ""}</td>
                <td>${materia.anioCursado ?? "-"}</td>
                <td>${materia.cuatrimestre ?? "-"}</td>
                <td>${materia.cargaHoraria ?? "-"}</td>
            </tr>`)
        .join("");
}

function filaVacia(tbody, columnas, texto) {
    tbody.innerHTML = `<tr><td colspan="${columnas}" class="celda-vacia">${texto}</td></tr>`;
}

// ============================================================
// Arranque
// ============================================================
cargarPerfil();
cargarHistorial();
cargarMateriasInscriptas();
cargarPlan();

// ============================================================
// Solicitar boleto estudiantil
// ============================================================
document.getElementById("btn-boleto").addEventListener("click", async () => {
    try {
        const respuesta = await fetch(API_BASE + "/alumnos/boleto/" + idAlumno, { 
            method: "POST", 
            headers: getAuthHeaders() 
        });
        if (!respuesta.ok) throw new Error(await extraerMensajeError(respuesta));
        mostrarMensaje("Solicitud de boleto estudiantil registrada.", "ok");
    } catch (error) {
        mostrarMensaje("No se pudo solicitar el boleto: " + error.message);
    }
});