DROP DATABASE IF EXISTS TrabajoFinal;
CREATE DATABASE IF NOT EXISTS TrabajoFinal;
USE TrabajoFinal;

-- USUARIO Y PERFILES

CREATE TABLE Persona (
	idPersona INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR (50) NOT NULL,
	apellido VARCHAR (50)NOT NULL,
	dni VARCHAR (50) NOT NULL UNIQUE,
	telefono VARCHAR (50),
	fechaNacimiento DATE,
    email VARCHAR (100)
);

CREATE TABLE Alumno (
	idAlumno INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idPersona INT UNSIGNED NOT NULL,
    legajo INT NOT NULL UNIQUE,
    anioIngreso YEAR,
    analiticoParcial BOOLEAN,
    esRegular BOOLEAN,
    planEstudio INT,
    promedio DOUBLE,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Administrador(
	idAdministrador INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idPersona INT UNSIGNED NOT NULL,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Profesor(
	idProfesor INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idPersona INT UNSIGNED NOT NULL,
    horasSemanales INT,
    estadoProfesor ENUM('ACTIVO', 'INACTIVO', 'LICENCIA') NOT NULL DEFAULT 'ACTIVO',
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

-- AREA ADMINISTRATIVA

CREATE TABLE Cuota(
	idCuota INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno INT UNSIGNED NOT NULL,
	valorCuota INT,
    fechaPago DATE,
    fechaVencimiento DATE,
    conceptoCuota ENUM('Cuota', 'Matricula') NOT NULL DEFAULT 'Cuota',
    estadoCuota ENUM('Pagada', 'Pendiente', 'Vencida') NOT NULL DEFAULT 'Pendiente',
	FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno) ON DELETE CASCADE
);

CREATE TABLE Alumno_Cursa_Carrera (
	idAlumno INT UNSIGNED NOT NULL,
    idCarrera INT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    PRIMARY KEY (idAlumno, idCarrera),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno),
    FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera)
);

-- AREA ACADEMICA

CREATE TABLE Carrera(
	idCarrera INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR (50),
    duracion INT,
    tituloOtorgado VARCHAR (50),
	modalidadCarrera ENUM ('Presencial', 'Virtual') NOT NULL DEFAULT 'Presencial',
    planDeEstudio year
);

CREATE TABLE Materia(
	idMateria INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
	idCarrera INT UNSIGNED NOT NULL,
    nombre VARCHAR(50),
    cargaHoraria INT,
    cuatrimestre INT,
    anioCursado YEAR,
	FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Examen(
	idExamen INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria INT UNSIGNED NOT NULL,
    fecha DATE,
    tiporExamen ENUM ('Parcial', 'Final') NOT NULL DEFAULT 'Parcial',
	FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Nota (
	idNota INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idExamen INT UNSIGNED NOT NULL,
    idAlumno INT UNSIGNED NOT NULL,
    nota INT,
    fechaRegistro DATE,
	FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE,
	FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno) ON DELETE CASCADE
);

CREATE TABLE Comision(
	idComision INT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria INT,
    idProfesor INT,
    nroComision INT,
    cantAlumnos INT,
    aula VARCHAR (50),
	FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE,
	FOREIGN KEY (idProfesor) REFERENCES Profesor(idProfesor) ON DELETE CASCADE
);

