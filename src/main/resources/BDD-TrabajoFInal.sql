DROP DATABASE IF EXISTS TrabajoFinal;
CREATE DATABASE IF NOT EXISTS TrabajoFinal;
USE TrabajoFinal;

-- ====================================================================
-- NIVEL 0: Tablas base (No dependen de ninguna otra)
-- ====================================================================

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    `role` VARCHAR(50)
);

CREATE TABLE carrera (
    idCarrera BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    duracion INT,
    tituloOtorgado VARCHAR(50),
    modalidadCarrera ENUM ('PRESENCIAL', 'VIRTUAL')
);

CREATE TABLE PreguntaEncuesta (
    idPreguntaEncuesta BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden INT NOT NULL,
    enunciado VARCHAR(300) NOT NULL
);

-- ====================================================================
-- NIVEL 1: Tablas que dependen del Nivel 0
-- ====================================================================

CREATE TABLE Persona (
    idPersona BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(50),
    fechaNacimiento DATE,
    email VARCHAR(100),
    rolUsuario ENUM('ALUMNO', 'PROFESOR', 'ADMIN'),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE PlanEstudio (
    idPlanEstudio BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    anioInicio INT NOT NULL,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE tokens (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    tokenType VARCHAR(50),
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE PeriodoInscripcion (
    idPeriodo BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    tipo ENUM('CURSADA','FINAL') NOT NULL,
    anioLectivo INT NOT NULL,
    cuatrimestre INT NOT NULL,
    fechaInicio DATETIME NOT NULL,
    fechaCierre DATETIME NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);

-- ====================================================================
-- NIVEL 2: Tablas que dependen del Nivel 1
-- ====================================================================

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

CREATE TABLE Materia (
    idMateria BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    idPlanEstudio BIGINT UNSIGNED,
    nombre VARCHAR(50),
    cargaHoraria INT,
    cuatrimestre INT,
    anioCursado INT,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE,
    FOREIGN KEY (idPlanEstudio) REFERENCES PlanEstudio(idPlanEstudio)
);

CREATE TABLE Aviso (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_persona BIGINT UNSIGNED NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    contenido VARCHAR(1000) NOT NULL,
    fecha_aviso DATETIME NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

-- ====================================================================
-- NIVEL 3: Tablas que dependen del Nivel 2
-- ====================================================================

CREATE TABLE Examen (
    idExamen BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    fecha DATE,
    tipoExamen ENUM ('PARCIAL', 'FINAL'),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE comision (
    idComision BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    idProfesor BIGINT UNSIGNED,
    nroComision INT,
    cantAlumnos INT DEFAULT 0,
    aula VARCHAR(50),
    cupo_maximo INT DEFAULT 50,
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria),
    FOREIGN KEY (idProfesor) REFERENCES Profesor(idPersona)
);

CREATE TABLE Cuota (
    idCuota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    mes INT NOT NULL,
    anio INT NOT NULL,
    valorCuota INT,
    fechaVencimiento DATE,
    conceptoCuota ENUM('CUOTA', 'MATRICULA'),
    estadoCuota ENUM('PAGADA', 'PENDIENTE', 'VENCIDA') NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

CREATE TABLE Alumno_Cursa_Carrera (
    idAlumno BIGINT UNSIGNED NOT NULL,
    idCarrera BIGINT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    PRIMARY KEY (idAlumno, idCarrera),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Correlatividad (
    idCorrelatividad BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    idMateriaCorrelativa BIGINT UNSIGNED NOT NULL,
    estadoParaCursar ENUM('CURSADA','APROBADA'),
    estadoParaRendir ENUM('CURSADA','APROBADA'),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE,
    FOREIGN KEY (idMateriaCorrelativa) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Alumno_Materia (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_alumno BIGINT UNSIGNED NOT NULL,
    id_materia BIGINT UNSIGNED NOT NULL,
    estado ENUM('PENDIENTE','CURSANDO','CURSADA','APROBADA','REGULAR') NOT NULL DEFAULT 'PENDIENTE',
    notaParcial1 DOUBLE DEFAULT NULL,
    notaParcial2 DOUBLE DEFAULT NULL,
    notaFinal DOUBLE DEFAULT NULL,
    fechaInscripcion DATE DEFAULT NULL,
    fechaRegularizacion DATE DEFAULT NULL,
    fechaAprobacion DATE DEFAULT NULL,
    UNIQUE KEY uq_alumno_materia (id_alumno, id_materia),
    FOREIGN KEY (id_alumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (id_materia) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE boleto_especial_educativo (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    fueSolicitado BOOLEAN DEFAULT FALSE,
    estaActivo BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

-- ====================================================================
-- NIVEL 4: Tablas que dependen del Nivel 3
-- ====================================================================

CREATE TABLE Nota (
    idNota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idExamen BIGINT UNSIGNED NOT NULL,
    idAlumno BIGINT UNSIGNED NOT NULL,
    nota INT,
    fechaRegistro DATE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

CREATE TABLE RespuestaEncuesta (
    idRespuestaEncuesta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idComision BIGINT UNSIGNED NOT NULL,
    hashAlumno VARCHAR(64) NOT NULL,
    fechaRespuesta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentarioFinal VARCHAR(1000) NULL,
    CONSTRAINT fk_re_comision FOREIGN KEY (idComision) REFERENCES comision(idComision),
    CONSTRAINT uq_hash_comision UNIQUE (idComision, hashAlumno)
);

CREATE TABLE Alumno_Inscripcion_Comision (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idComision BIGINT UNSIGNED NOT NULL,
    fechaInscripcion DATE NOT NULL,
    UNIQUE KEY uq_alumno_comision (idAlumno, idComision),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);

CREATE TABLE Alumno_Inscripcion_Examen_Final (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idExamen BIGINT UNSIGNED NOT NULL,
    fechaInscripcion DATE NOT NULL,
    UNIQUE KEY uq_alumno_examen (idAlumno, idExamen),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE
);

CREATE TABLE ComisionHorario (
    idComisionHorario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idComision BIGINT UNSIGNED NOT NULL,
    diaSemana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO') NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);

CREATE TABLE PagoCuota (
    idPagoCuota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idCuota BIGINT UNSIGNED NOT NULL,
    montoPagado INT NOT NULL,
    fechaPago DATE NOT NULL,
    metodoPago ENUM('EFECTIVO','TRANSFERENCIA','TARJETA'),
    FOREIGN KEY (idCuota) REFERENCES Cuota(idCuota) ON DELETE CASCADE
);

CREATE TABLE periodo_inscripcion_comision (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idPeriodo BIGINT UNSIGNED NOT NULL,
    idComision BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (idPeriodo) REFERENCES PeriodoInscripcion(idPeriodo) ON DELETE CASCADE,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);

-- ====================================================================
-- NIVEL 5: Tablas que dependen del Nivel 4
-- ====================================================================

CREATE TABLE RespuestaPregunta (
    idRespuestaPregunta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idRespuestaEncuesta BIGINT UNSIGNED NOT NULL,
    idPreguntaEncuesta BIGINT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    CONSTRAINT fk_rp_respuesta FOREIGN KEY (idRespuestaEncuesta) REFERENCES RespuestaEncuesta(idRespuestaEncuesta),
    CONSTRAINT fk_rp_pregunta FOREIGN KEY (idPreguntaEncuesta) REFERENCES PreguntaEncuesta(idPreguntaEncuesta)
);


-- ====================================================================
-- INSERTS DE CONFIGURACIÓN Y ENTIDADES BASE
-- ====================================================================

INSERT INTO PreguntaEncuesta (orden, enunciado) VALUES
(1, '¿El profesor cumplió con los temas del programa?'),
(2, '¿El profesor explicó con claridad?'),
(3, '¿El profesor estuvo disponible para consultas?'),
(4, '¿La cursada cumplió tus expectativas generales?'),
(5, '¿Recomendarías esta materia/comisión?');

INSERT INTO Carrera (nombre,duracion,tituloOtorgado,modalidadCarrera) VALUES
('Analista Programador Universitario',3,'Analista Programador Universitario','PRESENCIAL');

INSERT INTO PlanEstudio (idCarrera,nombre,anioInicio) VALUES
(1,'Plan 2025',2025);

-- PROFESOR PRUEBA (ID Persona = 1)
INSERT INTO Persona (nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario,user_id) VALUES
('Juan','Perez','30111222','2235551234','1985-05-10','juan.perez@gmail.com','PROFESOR', NULL);

INSERT INTO Profesor (idPersona,horasSemanales,estadoProfesor) VALUES
(1,20,'ACTIVO');

INSERT INTO Materia (idCarrera,nombre,cargaHoraria,cuatrimestre,anioCursado) VALUES
(1,'Programacion I',96,1,1),
(1,'Base de Datos I',96,2,1),
(1,'Programacion II',128,1,2);

INSERT INTO Comision (idMateria,idProfesor,nroComision,cantAlumnos,aula) VALUES
(1,1,101,35,'Aula 1'),
(2,1,102,30,'Aula 2'),
(3,1,103,28,'Aula 3');

-- ALUMNO 1: Pedro (ID Persona = 2)
INSERT INTO Persona (nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario,user_id) VALUES
('Pedro','Gomez','40111222','2234441111','2004-03-15','pedro@gmail.com','ALUMNO', NULL);

INSERT INTO Alumno (idPersona,legajo,anioIngreso,analiticoParcial,esRegular,promedio) VALUES
(2,1001,2025,TRUE,TRUE,8.5);


-- ====================================================================
-- USUARIOS DE AUTENTICACIÓN (Contraseña: 'PasswordSegura_2026' en BCrypt)
-- ====================================================================

INSERT INTO users (id, username, password, email, `role`) VALUES
(8, 'juan111', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'juan@correo.com', 'ALUMNO'),
(10, 'marialopez99', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'maria.lopez@correo.com', 'ALUMNO');


-- ====================================================================
-- ALUMNO 2 Y 3: Perfiles de Alumno Vinculados a sus Users
-- ====================================================================

-- Perfil María López (ID Persona = 3, user_id = 10)
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(3, 'Maria', 'Lopez', '40222333', '2234442222', '2003-08-20', 'maria.lopez@correo.com', 'ALUMNO', 10);

INSERT INTO Alumno (idPersona, legajo, anioIngreso, analiticoParcial, esRegular, promedio) VALUES
(3, 1002, 2024, TRUE, TRUE, 9.1);

-- Perfil Juan Alumno (ID Persona = 4, user_id = 8)
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id)
VALUES (4, 'Juan', 'Alumno', '40555666', '2234447777', '2002-05-12', 'juan@correo.com', 'ALUMNO', 8);

INSERT INTO Alumno (idPersona, legajo, anioIngreso, analiticoParcial, esRegular, promedio)
VALUES (4, 1003, 2024, TRUE, TRUE, 8.7);


-- ====================================================================
-- COMPLEMENTOS ACADÉMICOS Y TRANSACCIONALES
-- ====================================================================

INSERT INTO Alumno_Cursa_Carrera (idAlumno,idCarrera,fecha_inscripcion) VALUES
(2,1,'2025-03-01'),
(3,1,'2024-03-01'),
(4,1,'2024-03-01');

INSERT INTO Examen (idMateria,fecha,tipoExamen) VALUES
(1,'2026-06-20','PARCIAL'),
(1,'2026-07-10','FINAL');

INSERT INTO Nota (idExamen,idAlumno,nota,fechaRegistro) VALUES
(1,2,8,'2026-06-20'),
(1,3,10,'2026-06-20'),
(1,4,9,'2026-06-20');

INSERT INTO Cuota (idAlumno, valorCuota, fechaVencimiento, mes, anio, conceptoCuota, estadoCuota) VALUES
(2, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PAGADA'),
(3, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PENDIENTE'),
(4, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PENDIENTE');

INSERT INTO PagoCuota (idCuota, montoPagado, fechaPago, metodoPago) VALUES
(1, 30000, '2026-06-01', 'EFECTIVO');

INSERT INTO Alumno_Inscripcion_Comision (idAlumno,idComision,fechaInscripcion) VALUES
(2,1,'2026-06-15'),
(2,2,'2026-06-15'),
(3,1,'2026-06-15'),
(4,1,'2026-06-15');

INSERT INTO Alumno_Inscripcion_Examen_Final (idAlumno, idExamen, fechaInscripcion) VALUES
(2,2,'2026-07-01'),
(3,2,'2026-07-01'),
(4,2,'2026-07-01');

INSERT INTO Correlatividad (idMateria,idMateriaCorrelativa,estadoParaCursar,estadoParaRendir) VALUES
(3,1,'CURSADA','APROBADA');

INSERT INTO PeriodoInscripcion (idCarrera,tipo,anioLectivo,cuatrimestre,fechaInicio,fechaCierre,activa) VALUES
(1,'CURSADA',2026,1,'2026-06-01 00:00:00','2026-07-01 00:00:00',true);

DELETE FROM Alumno_Materia WHERE id_alumno = 4;

-- Insertamos con estados que seguro tu backend procesa mejor (CURSANDO / APROBADA)
INSERT INTO Alumno_Materia (id_alumno, id_materia, estado, notaParcial1, notaParcial2, notaFinal, fechaInscripcion) VALUES
(4, 1, 'APROBADA', 8.0, 7.0, 8.0, '2026-03-01'),
(4, 2, 'CURSANDO', NULL, NULL, NULL, '2026-03-01');


INSERT INTO users (id, username, password, email, `role`) VALUES
(11, 'carlosprof', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'carlos.profesor@correo.com', 'PROFESOR');

-- 2. Creamos los datos de Persona para Carlos (ID Persona = 5) vinculado al user_id = 11
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(5, 'Carlos', 'Maestro', '30999888', '2234449999', '1980-04-25', 'carlos.profesor@correo.com', 'PROFESOR', 11);

-- 3. Creamos su perfil en la tabla Profesor
INSERT INTO Profesor (idPersona, horasSemanales, estadoProfesor) VALUES
(5, 40, 'ACTIVO');

-- 1. Creamos el usuario en la tabla users (ID = 12, Rol = ADMIN)
INSERT INTO users (id, username, password, email, `role`) VALUES
(12, 'admin_general', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'admin@correo.com', 'ADMIN');

-- 2. Creamos los datos de Persona para el Admin (ID Persona = 6) vinculado al user_id = 12
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(6, 'Ana', 'Administradora', '30111000', '2234440000', '1988-09-15', 'admin@correo.com', 'ADMIN', 12);

-- 3. Creamos su perfil en la tabla Administrador
INSERT INTO Administrador (idPersona) VALUES
(6);

UPDATE users SET password = 'PasswordSegura_2026' WHERE id IN (8, 10, 11, 12);

UPDATE users SET password = '$2a$10$X5pZg7Uv8Ew9WhP1E4SgCOsH9CbyFpWlW73u5Y6p5D9m8I3D2jXzq' WHERE id IN (8, 10, 11, 12);

UPDATE users
SET password = '$2a$10$7R8MByD4X9wZ.F56qE0uX.bZg7G71Wv8p3y1GfSmT3f6W6hV6eW2a'
WHERE id IN (8, 10, 11, 12);

UPDATE users SET password = '$2a$10$uVshZ2pZ6pS6Kx8Gk7eYI.f3l1pXN2Z4V7jN9vC7mD5vH2vE4S2v2' WHERE id IN (8, 10, 11, 12);

SELECT password FROM users WHERE email = 'juan@correo.com';


-- ---------------------------------

SELECT id, email, password, role
FROM users
WHERE email = 'admin@correo.com';

SELECT id, email, password, role
FROM users
WHERE email = 'carlos.profesor@correo.com';

UPDATE users
SET password = '$2a$10$IuAVQPzflxN3qfsD55Fi4OCuj0YXI8KX9Y.jEqEKgaOkkLaoBMje6'
WHERE id IN (8,10,11,12);

SELECT email, password
FROM users
WHERE email = 'carlos.profesor@correo.com';

select * from alumno;
select * from persona;
select * from comision;
select * from users;
select * from alumno;
select * from carrera;
select * from Cuota;
select * from Users;