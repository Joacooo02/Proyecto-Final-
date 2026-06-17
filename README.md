# 🎓 Gestor de Universidad - Proyecto Final

![Status](https://img.shields.io/badge/Status-En%20Desarrollo-green)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)

Sistema integral de gestión académica diseñado para facilitar la administración de una institución universitaria. Esta plataforma centraliza la interacción entre el alumnado, el cuerpo docente y el personal administrativo, gestionando inscripciones, exámenes y el seguimiento del estado académico a través de un sistema de roles y permisos.

## 👥 Roles y Funcionalidades Principales

El sistema está protegido mediante autenticación y autorización, dividiendo el acceso en tres perfiles principales:

*   **👨‍🎓 Alumno:** Inscripciones, consulta de estado académico, historial y correlativas.
*   **👨‍🏫 Profesor:** Carga de exámenes, gestión de notas y visualización de comisiones.
*   **🛡️ Administrador:** ABM integral de usuarios, materias, carreras y comisiones.

## 📋 Requisitos del Sistema

Para conocer el alcance detallado del proyecto, puedes desplegar los siguientes bloques donde se especifican los Requisitos Funcionales (RF) agrupados por actor, y los Requisitos No Funcionales (RNF) de arquitectura.

### Requisitos Funcionales (RF)

<details>
<summary><b>Generales (Todos los usuarios)</b></summary>

*   **RF01:** El sistema debe permitir al Administrador, Profesor y Alumno iniciar sesión.
*   **RF02:** El sistema debe permitir al Administrador, Profesor y Alumno cerrar sesión.
*   **RF03:** El sistema debe permitir al Administrador, Profesor y Alumno cambiar su contraseña.
*   **RF04:** El sistema debe permitir al Administrador, Profesor y Alumno hacer ABM de Perfil.
*   **RF05:** El sistema debe permitir al Administrador, Profesor y Alumno visualizar su perfil.
</details>

<details>
<summary><b>Administrador</b></summary>

*   **RF06:** Realizar ABM de profesores.
*   **RF07:** Visualizar la lista de Profesores.
*   **RF08:** Filtrar la lista de Profesores.
*   **RF09:** Visualizar el perfil de Profesores.
*   **RF10:** Realizar ABM de Alumnos.
*   **RF11:** Visualizar la lista de Alumnos.
*   **RF12:** Filtrar la lista de Alumnos.
*   **RF13:** Visualizar el perfil del Alumno.
*   **RF14:** Realizar ABM de materias.
*   **RF15:** Visualizar la lista de materias.
*   **RF16:** Filtrar la lista de materias.
*   **RF17:** Visualizar detalle de Materias.
*   **RF18:** Realizar ABM de comisiones.
*   **RF19:** Visualizar la lista de comisiones.
*   **RF20:** Filtrar la lista de comisiones.
*   **RF21:** Visualizar detalle de comisiones.
*   **RF22:** Realizar ABM de carreras.
*   **RF23:** Visualizar lista de carreras.
*   **RF24:** Filtrar la lista de carreras.
*   **RF25:** Visualizar detalle de carreras.
*   **RF26:** Crear una encuesta.
</details>

<details>
<summary><b>Profesor</b></summary>

*   **RF27:** Visualizar la lista de materias que tiene asignadas.
*   **RF28:** Visualizar la lista de comisiones que tiene asignadas.
*   **RF29:** Visualizar la lista de Alumnos que están inscriptos en una materia.
*   **RF30:** Agregar un exámen.
*   **RF31:** Visualizar detalles de un examen.
*   **RF32:** Visualizar la lista de examen.
*   **RF33:** Filtrar la lista de examen.
*   **RF34:** Cargar las notas de un examen.
</details>

<details>
<summary><b>Alumno</b></summary>

*   **RF35:** Visualizar la lista de materias del plan académico.
*   **RF36:** Visualizar su historial académico.
*   **RF37:** Visualizar su historial de exámenes.
*   **RF38:** Visualizar la lista de materias en las que está inscripto.
*   **RF39:** Visualizar la lista de correlatividades para cursar.
*   **RF40:** Visualizar la lista de correlatividades para rendir un examen.
*   **RF41:** Visualizar la lista de materias a las que se puede inscribir.
*   **RF42:** Inscribirse en una materia.
*   **RF43:** Visualizar la lista de comisiones a las que se puede inscribir de una materia en específico.
*   **RF44:** Descargar certificados.
*   **RF45:** Visualizar la lista de cuotas.
*   **RF46:** Realizar el pago de una cuota.
*   **RF47:** Contestar una encuesta.
*   **RF48:** Inscribirse en exámenes.
*   **RF49:** Visualizar avisos.
*   **RF50:** Solicitar el Boleto Especial Educativo.
</details>

### Requisitos No Funcionales (RNF)

*   **RNF01 (Usabilidad):** El sistema debe tener una interfaz intuitiva y fácil de entender para cualquier usuario.
*   **RNF02 (Disponibilidad):** El sistema debe estar disponible en todo momento, sobre todo en fechas de inscripciones.
*   **RNF03 (Rendimiento):** El sistema debe tener tiempos de carga menores a 2 segundos.
*   **RNF04 (Persistencia):** El sistema debe almacenar todos sus registros en una base de datos relacional.
*   **RNF05 (Escalabilidad):** El sistema debe soportar al menos 1000 usuarios simultáneos.
*   **RNF06 (Integridad):** El sistema debe asegurar la integridad de los datos de los usuarios registrados.
*   **RNF07 (Seguridad):** El sistema debe contar con autenticación segura mediante usuarios y contraseñas cifradas. 

## 🛠️ Stack Tecnológico

*   **Backend:** Java, Spring Boot
*   **Seguridad:** Spring Security
*   **Base de Datos:** MySQL
*   **Gestor de Dependencias:** Maven
*   **Documentación:** Swagger / OpenAPI 3

## ⚙️ Requisitos Previos e Instalación Local

Para ejecutar este proyecto en un entorno local, necesitas tener instalado **Java JDK 17+**, **Maven**, **MySQL Server** y **Git**.

1. **Clonar el repositorio:**
```bash
   git clone [https://github.com/Joacooo02/Proyecto-Final-.git](https://github.com/Joacooo02/Proyecto-Final-.git)
   cd Proyecto-Final-
