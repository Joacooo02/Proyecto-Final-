// ============================================================
// Navbar del admin (componente unico)
// ------------------------------------------------------------
// En vez de copiar la barra de navegacion en cada pagina, cada pagina del
// admin tiene <header class="barra-superior" id="barra-admin"></header> y este
// script la rellena al cargar.
//
// Para AGREGAR, QUITAR o RENOMBRAR una seccion: editar SOLO este array.
// El link "activo" se calcula solo, segun el archivo de la pagina actual.
// ============================================================
(function () {
    const BASE = "/pages/Administrador/";

    // [archivo, etiqueta] en el orden en que aparecen en la barra.
    const secciones = [
        ["menu_admin.html", "Menu"],
        ["gestion_alumnos_admin.html", "Alumnos"],
        ["gestion_profesores_admin.html", "Profesores"],
        ["gestion_carreras_admin.html", "Carreras"],
        ["gestion_materias_admin.html", "Materias"],
        ["gestion_comisiones_admin.html", "Comisiones"],
        ["gestion_examenes_admin.html", "Examenes"],
        ["gestion_correlatividades_admin.html", "Correlatividades"],
        ["gestion_planes_admin.html", "Planes"],
        ["gestion_horarios_admin.html", "Horarios"],
        ["gestion_avisos_admin.html", "Avisos"],
        ["gestion_alumno_materia_admin.html", "Alumno-Materia"],
        ["gestion_cuotas_admin.html", "Cuotas"],
        ["gestion_usuarios_admin.html", "Usuarios"],
        ["gestion_inscripciones_admin.html", "Inscripciones"],
    ];

    // Nombre del archivo de la pagina actual (ultimo segmento de la URL).
    const actual = window.location.pathname.split("/").pop();

    const links = secciones
        .map(([archivo, etiqueta]) => {
            const activo = archivo === actual ? ' class="activo"' : "";
            return `<a href="${BASE}${archivo}"${activo}>${etiqueta}</a>`;
        })
        .join("");

    // El handler del logout (limpia el token JWT) lo engancha sesion_admin.js sobre #nav-salir.
    const salir = `<a href="#" id="nav-salir" class="navbar-salir">Salir</a>`;

    const barra = document.getElementById("barra-admin");
    if (barra) {
        barra.innerHTML =
            `<span class="marca">Atenea</span><nav class="navbar">${links}${salir}</nav>`;
    }
})();
