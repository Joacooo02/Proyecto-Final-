// ============================================================
// Navbar del portal del alumno (componente unico)
// ------------------------------------------------------------
// Cada pagina del alumno tiene <header class="barra-superior" id="barra-alumno">
// y este script la rellena. Para agregar/quitar/renombrar una seccion, editar
// SOLO este array. El link "activo" se calcula segun la pagina actual.
//
// Importante: este script se carga ANTES que sesion_alumno.js, asi cuando
// sesion_alumno.js engancha el handler del boton "Salir" (#nav-salir), el link
// ya esta inyectado en el DOM.
// ============================================================
(function () {
    const BASE = "/pages/Alumno/";

    const secciones = [
        ["menu_alumno.html", "Menu"],
        ["mis_datos_alumno.html", "Mis datos"],
        ["inscripciones_alumno.html", "Inscripciones"],
        ["cuotas_alumno.html", "Cuotas"],
        ["encuestas_alumno.html", "Encuestas"],
    ];

    const actual = window.location.pathname.split("/").pop();

    const links = secciones
        .map(([archivo, etiqueta]) => {
            const activo = archivo === actual ? ' class="activo"' : "";
            return `<a href="${BASE}${archivo}"${activo}>${etiqueta}</a>`;
        })
        .join("");

    // El handler del logout lo engancha sesion_alumno.js sobre #nav-salir.
    const salir = `<a href="#" id="nav-salir" class="navbar-salir">Salir</a>`;

    const barra = document.getElementById("barra-alumno");
    if (barra) {
        barra.innerHTML =
            `<span class="marca">Atenea</span><nav class="navbar">${links}${salir}</nav>`;
    }
})();
