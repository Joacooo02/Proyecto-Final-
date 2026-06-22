// ============================================================
// Navbar del portal del profesor (componente unico)
// ------------------------------------------------------------
// Cada pagina del profesor tiene <header class="barra-superior" id="barra-profesor">
// y este script la rellena. Para agregar/quitar/renombrar una seccion, editar
// SOLO este array. El link "activo" se calcula segun la pagina actual.
//
// Se carga ANTES que sesion_profesor.js (que engancha el boton "Salir").
// ============================================================
(function () {
    const BASE = "/pages/Profesor/";

    const secciones = [
        ["menu_profesor.html", "Menu"],
        ["mis_comisiones_profesor.html", "Mis comisiones"],
        ["cargar_notas_profesor.html", "Cargar notas"],
        ["encuestas_profesor.html", "Encuestas"],
    ];

    const actual = window.location.pathname.split("/").pop();

    const links = secciones
        .map(([archivo, etiqueta]) => {
            const activo = archivo === actual ? ' class="activo"' : "";
            return `<a href="${BASE}${archivo}"${activo}>${etiqueta}</a>`;
        })
        .join("");

    const salir = `<a href="#" id="nav-salir" class="navbar-salir">Salir</a>`;

    const barra = document.getElementById("barra-profesor");
    if (barra) {
        barra.innerHTML =
            `<span class="marca">Atenea</span><nav class="navbar">${links}${salir}</nav>`;
    }
})();
