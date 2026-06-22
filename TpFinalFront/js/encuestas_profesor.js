// ============================================================
// Encuestas (profesor) - resultados
// Usa el endpoint "de resultados" de EncuestaController:
//   GET /encuestas/{idComision}/resultados -> { enunciado: promedio, ... }
//
// El backend devuelve un objeto (Map) con el promedio (1 a 5) de cada pregunta.
// ============================================================

SesionProfesor.requerir();

const inputComision = document.getElementById("input-comision");
const btnVer = document.getElementById("btn-ver");
const tbody = document.getElementById("tbody-resultados");

async function verResultados() {
    const idComision = inputComision.value.trim();
    if (idComision === "") {
        mostrarMensaje("Ingresa el id de la comision.");
        return;
    }

    try {
        const respuesta = await fetch(API_BASE + "/encuestas/" + idComision + "/resultados");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const resultados = await respuesta.json();
        mostrarResultados(resultados);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los resultados: " + error.message);
    }
}

function mostrarResultados(resultados) {
    tbody.innerHTML = "";
    const entradas = Object.entries(resultados || {});
    if (entradas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="2" class="celda-vacia">Esta comision no tiene respuestas todavia.</td></tr>`;
        return;
    }
    tbody.innerHTML = entradas
        .map(
            ([enunciado, promedio]) => `
            <tr>
                <td>${enunciado}</td>
                <td>${Number(promedio).toFixed(2)}</td>
            </tr>`
        )
        .join("");
}

btnVer.addEventListener("click", verResultados);
inputComision.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") verResultados();
});
