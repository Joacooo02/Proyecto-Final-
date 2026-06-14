DROP DATABASE IF EXISTS TrabajoFinal;
CREATE DATABASE IF NOT EXISTS TrabajoFinal;
USE TrabajoFinal;


CREATE TABLE Persona (
    idPersona BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(50),
    fechaNacimiento DATE,
    email VARCHAR(100),
    rolUsuario ENUM('ALUMNO', 'PROFESOR', 'ADMIN')
);

CREATE TABLE Alumno (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    legajo BIGINT NOT NULL UNIQUE,
    anioIngreso INT,
    analiticoParcial BOOLEAN,
    esRegular BOOLEAN,
    promedio DOUBLE,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Administrador (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Profesor (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    horasSemanales INT,
    estadoProfesor ENUM('ACTIVO', 'INACTIVO', 'LICENCIA') NOT NULL DEFAULT 'ACTIVO',
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

-- AREA ACADEMICA

CREATE TABLE Carrera(
    idCarrera BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    duracion INT,
    tituloOtorgado VARCHAR(50),
    modalidadCarrera ENUM ('PRESENCIAL', 'VIRTUAL')
);

CREATE TABLE Materia(
    idMateria BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(50),
    cargaHoraria INT,
    cuatrimestre INT,
    anioCursado INT,
    FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Examen(
    idExamen BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    fecha DATE,
    tipoExamen ENUM ('PARCIAL', 'FINAL'),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Comision(
    idComision BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED,
    idProfesor BIGINT UNSIGNED,
    nroComision INT,
    cantAlumnos INT,
    aula VARCHAR(50),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE,
    FOREIGN KEY (idProfesor) REFERENCES Profesor(idPersona) ON DELETE CASCADE
);

-- AREA ADMINISTRATIVA

CREATE TABLE Cuota(
    idCuota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
	mes INT NOT NULL,
    anio INT NOT NULL,
    valorCuota INT,
    fechaPago DATE,
    fechaVencimiento DATE,
    conceptoCuota ENUM('CUOTA', 'MATRICULA'),
    estadoCuota ENUM('PAGADA', 'PENDIENTE', 'VENCIDA') NOT NULL DEFAULT 'PENDIENTE',

    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona)
	ON DELETE CASCADE
);

CREATE TABLE Alumno_Cursa_Carrera (
    idAlumno BIGINT UNSIGNED NOT NULL,
    idCarrera BIGINT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    PRIMARY KEY (idAlumno, idCarrera),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Nota (
    idNota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idExamen BIGINT UNSIGNED NOT NULL,
    idAlumno BIGINT UNSIGNED NOT NULL,
    nota INT,
    fechaRegistro DATE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

CREATE TABLE PreguntaEncuesta (
    idPreguntaEncuesta  BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden       INT          NOT NULL,
    enunciado   VARCHAR(300) NOT NULL
);

INSERT INTO PreguntaEncuesta (orden, enunciado) VALUES
(1, '¿El profesor cumplió con los temas del programa?'),
(2, '¿El profesor explicó con claridad?'),
(3, '¿El profesor estuvo disponible para consultas?'),
(4, '¿La cursada cumplió tus expectativas generales?'),
(5, '¿Recomendarías esta materia/comisión?');

CREATE TABLE RespuestaEncuesta (
    idRespuestaEncuesta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idComision BIGINT UNSIGNED NOT NULL,
    hashAlumno VARCHAR(64) NOT NULL,
    fechaRespuesta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentarioFinal VARCHAR(1000) NULL,
    CONSTRAINT fk_re_comision  FOREIGN KEY (idComision) REFERENCES Comision(idComision),
    CONSTRAINT uq_hash_comision UNIQUE (idComision, hashAlumno)
);

CREATE TABLE RespuestaPregunta (
    idRespuestaPregunta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idRespuestaEncuesta BIGINT UNSIGNED NOT NULL,
    idPreguntaEncuesta BIGINT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    CONSTRAINT fk_rp_respuesta FOREIGN KEY (idRespuestaEncuesta) REFERENCES RespuestaEncuesta(idRespuestaEncuesta),
    CONSTRAINT fk_rp_pregunta  FOREIGN KEY (idPreguntaEncuesta) REFERENCES PreguntaEncuesta(idPreguntaEncuesta)
);

CREATE TABLE Alumno_Inscripcion_Materia (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idMateria BIGINT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Alumno_Inscripcion_Comision (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idComision BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idComision) REFERENCES Comision(idComision) ON DELETE CASCADE
);

CREATE TABLE Alumno_Inscripcion_Examen_Final (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idExamen BIGINT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE
);


-- ---------------------------------------------- ESTO ES LO NUEVO QUE AGREGAMOS ----------------------------------------------
CREATE TABLE PlanEstudio(
    idPlanEstudio BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    anioInicio INT NOT NULL,

    FOREIGN KEY (idCarrera)
        REFERENCES Carrera(idCarrera)
        ON DELETE CASCADE
);

ALTER TABLE Materia
ADD COLUMN idPlanEstudio BIGINT UNSIGNED;

ALTER TABLE Materia
ADD CONSTRAINT fk_materia_plan
FOREIGN KEY (idPlanEstudio)
REFERENCES PlanEstudio(idPlanEstudio);

CREATE TABLE Correlatividad(
    idCorrelatividad BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idMateria BIGINT UNSIGNED NOT NULL,
    idMateriaCorrelativa BIGINT UNSIGNED NOT NULL,

    estadoParaCursar ENUM('CURSADA','APROBADA'),
    estadoParaRendir ENUM('CURSADA','APROBADA'),

    FOREIGN KEY (idMateria)
        REFERENCES Materia(idMateria)
        ON DELETE CASCADE,

    FOREIGN KEY (idMateriaCorrelativa)
        REFERENCES Materia(idMateria)
        ON DELETE CASCADE
);

CREATE TABLE Alumno_Materia(
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idAlumno BIGINT UNSIGNED NOT NULL,
    idMateria BIGINT UNSIGNED NOT NULL,

    estado ENUM(
        'PENDIENTE',
        'CURSANDO',
        'CURSADA',
        'APROBADA'
    ) NOT NULL DEFAULT 'PENDIENTE',

    notaFinal DOUBLE,
    fechaAprobacion DATE,

    UNIQUE(idAlumno,idMateria),

    FOREIGN KEY (idAlumno)
        REFERENCES Alumno(idPersona)
        ON DELETE CASCADE,

    FOREIGN KEY (idMateria)
        REFERENCES Materia(idMateria)
        ON DELETE CASCADE
);

CREATE TABLE ComisionHorario(
    idComisionHorario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idComision BIGINT UNSIGNED NOT NULL,

    diaSemana ENUM(
        'LUNES',
        'MARTES',
        'MIERCOLES',
        'JUEVES',
        'VIERNES',
        'SABADO'
    ) NOT NULL,

    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,

    FOREIGN KEY (idComision)
        REFERENCES Comision(idComision)
        ON DELETE CASCADE
);

CREATE TABLE PeriodoInscripcion(
    idPeriodo BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idCarrera BIGINT UNSIGNED NOT NULL,

    tipo ENUM('CURSADA','FINAL') NOT NULL,

    anioLectivo INT NOT NULL,
    cuatrimestre INT NOT NULL,

    fechaInicio DATETIME NOT NULL,
    fechaCierre DATETIME NOT NULL,

    activa BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (idCarrera)
        REFERENCES Carrera(idCarrera)
        ON DELETE CASCADE
);


-- ---------------------------------------------- ESTO ES LO NUEVO QUE AGREGAMOS ----------------------------------------------


-- ------------------------------------------TODO ESTO ES DE PRUEBA ----------------------------------------------------------
-- CARRERA
INSERT INTO Carrera
(nombre,duracion,tituloOtorgado,modalidadCarrera)
VALUES
('Analista Programador Universitario',3,'Analista Programador Universitario','PRESENCIAL');

-- PLAN DE ESTUDIO
INSERT INTO PlanEstudio
(idCarrera,nombre,anioInicio)
VALUES
(1,'Plan 2025',2025);

-- PERSONA PROFESOR
INSERT INTO Persona
(nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario)
VALUES
('Juan','Perez','30111222','2235551234','1985-05-10','juan.perez@gmail.com','PROFESOR');

-- PROFESOR
INSERT INTO Profesor
(idPersona,horasSemanales,estadoProfesor)
VALUES
(1,20,'ACTIVO');

-- MATERIAS
INSERT INTO Materia
(idCarrera,nombre,cargaHoraria,cuatrimestre,anioCursado)
VALUES
(1,'Programacion I',96,1,1),
(1,'Base de Datos I',96,2,1),
(1,'Programacion II',128,1,2);

-- COMISIONES
INSERT INTO Comision
(idMateria,idProfesor,nroComision,cantAlumnos,aula)
VALUES
(1,1,101,35,'Aula 1'),
(2,1,102,30,'Aula 2'),
(3,1,103,28,'Aula 3');

-- ALUMNO 1
INSERT INTO Persona
(nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario)
VALUES
('Pedro','Gomez','40111222','2234441111','2004-03-15','pedro@gmail.com','ALUMNO');

INSERT INTO Alumno
(idPersona,legajo,anioIngreso,analiticoParcial,esRegular,promedio)
VALUES
(2,1001,2025,TRUE,TRUE,8.5);

-- ALUMNO 2
INSERT INTO Persona
(nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario)
VALUES
('Maria','Lopez','40222333','2234442222','2003-08-20','maria@gmail.com','ALUMNO');

INSERT INTO Alumno
(idPersona,legajo,anioIngreso,analiticoParcial,esRegular,promedio)
VALUES
(3,1002,2024,TRUE,TRUE,9.1);

-- INSCRIPCION A CARRERA
INSERT INTO Alumno_Cursa_Carrera
(idAlumno,idCarrera,fecha_inscripcion)
VALUES
(2,1,'2025-03-01'),
(3,1,'2024-03-01');

-- EXAMEN PARCIAL
INSERT INTO Examen
(idMateria,fecha,tipoExamen)
VALUES
(1,'2026-06-20','PARCIAL');

-- EXAMEN FINAL
INSERT INTO Examen
(idMateria,fecha,tipoExamen)
VALUES
(1,'2026-07-10','FINAL');

-- NOTAS
INSERT INTO Nota
(idExamen,idAlumno,nota,fechaRegistro)
VALUES
(1,2,8,'2026-06-20'),
(1,3,10,'2026-06-20');

-- CUOTAS
INSERT INTO Cuota (
    idAlumno,
    valorCuota,
    fechaPago,
    fechaVencimiento,
    mes,
    anio,
    conceptoCuota,
    estadoCuota
)
VALUES
(2, 30000, '2026-06-01', '2026-06-10', 6, 2026, 'CUOTA', 'PAGADA'),
(3, 30000, NULL, '2026-06-10', 6, 2026, 'CUOTA', 'PENDIENTE');

-- INSCRIPCION A MATERIAS
INSERT INTO Alumno_Inscripcion_Materia
(idAlumno,idMateria,fecha_inscripcion)
VALUES
(2,1,'2025-03-10'),
(2,2,'2025-03-12'),
(2,3,'2025-03-15'),
(3,1,'2024-03-10'),
(3,3,'2024-03-12');

-- INSCRIPCION A COMISIONES
INSERT INTO Alumno_Inscripcion_Comision
(idAlumno,idComision)
VALUES
(2,1),
(2,2),
(3,1);

-- INSCRIPCION A FINAL
INSERT INTO Alumno_Inscripcion_Examen_Final
(idAlumno,idExamen,fecha_inscripcion)
VALUES
(2,2,'2026-07-01'),
(3,2,'2026-07-01');

-- CORRELATIVIDAD
INSERT INTO Correlatividad
(idMateria,idMateriaCorrelativa,estadoParaCursar,estadoParaRendir)
VALUES
(3,1,'CURSADA','APROBADA');

-- ACTUALIZAR COMISION
UPDATE Comision
SET cantAlumnos = 50
WHERE idComision = 1;


SELECT * FROM Persona;
SELECT * FROM Profesor;
SELECT * FROM Alumno;
SELECT * FROM Carrera;
SELECT * FROM Materia;
SELECT * FROM Comision;
SELECT * FROM Examen;
SELECT * FROM Nota;
SELECT * FROM Cuota;
SELECT * FROM Alumno_Cursa_Carrera;

 