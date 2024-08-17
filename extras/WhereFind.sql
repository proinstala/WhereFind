-- Crea la base de datos
CREATE DATABASE IF NOT EXISTS `WHERE_FIND_DATA`;

use `WHERE_FIND_DATA`;


-- Elimina las tablas
DROP TABLE IF EXISTS WHERE_FIND_DATA.`USER`;

-- Elimina las funciones
DROP FUNCTION IF EXISTS ENCRYPT_DATA_BASE64;
DROP FUNCTION IF EXISTS DECRYPT_DATA_BASE64;


DELIMITER //

CREATE FUNCTION ENCRYPT_DATA_BASE64(
    plaintext VARCHAR(255)
)
RETURNS TEXT
READS SQL DATA
BEGIN
    DECLARE encryption_key VARCHAR(32) DEFAULT '|--Where-Find--|';
    DECLARE encrypted_blob BLOB;
    DECLARE encrypted_base64 TEXT;

    -- Cifrar el texto usando AES_ENCRYPT con la clave constante
    SET encrypted_blob = AES_ENCRYPT(plaintext, encryption_key);

    -- Convertir el BLOB cifrado a Base64
    SET encrypted_base64 = TO_BASE64(encrypted_blob);

    -- Devolver el texto cifrado en Base64
    RETURN encrypted_base64;
END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION DECRYPT_DATA_BASE64(
    encrypted_base64 TEXT
)
RETURNS VARCHAR(255)
READS SQL DATA
BEGIN
    DECLARE encryption_key VARCHAR(32) DEFAULT '|--Where-Find--|';
    DECLARE decrypted_blob BLOB;
    DECLARE decrypted_text VARCHAR(255);

    -- Convertir el Base64 cifrado a BLOB
    SET decrypted_blob = FROM_BASE64(encrypted_base64);

    -- Descifrar el BLOB usando AES_DECRYPT con la clave constante
    SET decrypted_text = AES_DECRYPT(decrypted_blob, encryption_key);

    -- Devolver el texto descifrado
    RETURN decrypted_text;
END //

DELIMITER ;


-- Crea la tabla USER
CREATE TABLE IF NOT EXISTS USER (
	id INT auto_increment NOT NULL,
	user_name varchar(100) NOT NULL,
	password TEXT NOT NULL,
    rol varchar(100) NOT NULL,
	activo BOOL DEFAULT TRUE NOT NULL,
    nombre varchar(100) NOT NULL,
    apellidos varchar(100) NOT NULL,
    email varchar(200) NOT NULL,
    imagen MEDIUMTEXT,
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (user_name),
    CONSTRAINT UC_EMAIL UNIQUE (email)
);


-- Crea un usuarios de prueba
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('david', ENCRYPT_DATA_BASE64('123'), 'Admin', 1, 'David', 'Jiménez Alonso', 'david@email.es');
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('juanma', ENCRYPT_DATA_BASE64('123'), 'Admin', 1, 'Juan Manuel', 'Soltero Sánchez', 'juanma@email.es');

-- Elimina las tablas
DROP TABLE IF EXISTS WHERE_FIND_DATA.`RECOVERY`;

-- Elimina las funciones
DROP FUNCTION IF EXISTS RECOVERY_GET_INTENTOS;

DELIMITER $$

CREATE FUNCTION RECOVERY_GET_INTENTOS(hash_val VARCHAR(100))
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE intentos_actuales INT;

    -- Intenta insertar el nuevo registro
    INSERT INTO RECOVERY (hash, intentos)
    VALUES (hash_val, 1)
    ON DUPLICATE KEY UPDATE intentos = intentos + 1;

    -- Obtiene el valor de intentos después de la inserción/actualización
    SELECT intentos INTO intentos_actuales
    FROM RECOVERY
    WHERE hash = hash_val;

    -- Devuelve el número de intentos
    RETURN intentos_actuales;
END $$

DELIMITER ;


-- Crea la tabla RECOVERY
CREATE TABLE IF NOT EXISTS RECOVERY (
	id INT auto_increment NOT NULL,
	hash varchar(100) NOT NULL,
    intentos INT DEFAULT 1 NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_HASH UNIQUE (hash)
);


CREATE TABLE IF NOT EXISTS PROVINCIA (
	id INT auto_increment NOT NULL,
	nombre varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS LOCALIDAD (
	id INT auto_increment NOT NULL,
	nombre varchar(100) NOT NULL,
    provincia_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_LOCALIDAD_PROVINCIA FOREIGN KEY (provincia_id)
        REFERENCES PROVINCIA(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

/* Sentencias sql para rellenar de datos la tabla PROVINCIA */ 
INSERT INTO PROVINCIA (id, nombre) VALUES (1, 'Álava');
INSERT INTO PROVINCIA (id, nombre) VALUES (2, 'Albacete');
INSERT INTO PROVINCIA (id, nombre) VALUES (3, 'Alicante');
INSERT INTO PROVINCIA (id, nombre) VALUES (4, 'Almería');
INSERT INTO PROVINCIA (id, nombre) VALUES (5, 'Asturias');
INSERT INTO PROVINCIA (id, nombre) VALUES (6, 'Ávila');
INSERT INTO PROVINCIA (id, nombre) VALUES (7, 'Badajoz');
INSERT INTO PROVINCIA (id, nombre) VALUES (8, 'Baleares');
INSERT INTO PROVINCIA (id, nombre) VALUES (9, 'Barcelona');
INSERT INTO PROVINCIA (id, nombre) VALUES (10, 'Burgos');
INSERT INTO PROVINCIA (id, nombre) VALUES (11, 'Cáceres');
INSERT INTO PROVINCIA (id, nombre) VALUES (12, 'Cádiz');
INSERT INTO PROVINCIA (id, nombre) VALUES (13, 'Cantabria');
INSERT INTO PROVINCIA (id, nombre) VALUES (14, 'Castellón');
INSERT INTO PROVINCIA (id, nombre) VALUES (15, 'Ceuta');
INSERT INTO PROVINCIA (id, nombre) VALUES (16, 'Ciudad Real');
INSERT INTO PROVINCIA (id, nombre) VALUES (17, 'Córdoba');
INSERT INTO PROVINCIA (id, nombre) VALUES (18, 'Cuenca');
INSERT INTO PROVINCIA (id, nombre) VALUES (19, 'Gerona');
INSERT INTO PROVINCIA (id, nombre) VALUES (20, 'Granada');
INSERT INTO PROVINCIA (id, nombre) VALUES (21, 'Guadalajara');
INSERT INTO PROVINCIA (id, nombre) VALUES (22, 'Guipúzcoa');
INSERT INTO PROVINCIA (id, nombre) VALUES (23, 'Huelva');
INSERT INTO PROVINCIA (id, nombre) VALUES (24, 'Huesca');
INSERT INTO PROVINCIA (id, nombre) VALUES (25, 'Jaén');
INSERT INTO PROVINCIA (id, nombre) VALUES (26, 'La Coruña');
INSERT INTO PROVINCIA (id, nombre) VALUES (27, 'La Rioja');
INSERT INTO PROVINCIA (id, nombre) VALUES (28, 'Las Palmas');
INSERT INTO PROVINCIA (id, nombre) VALUES (29, 'León');
INSERT INTO PROVINCIA (id, nombre) VALUES (30, 'Lérida');
INSERT INTO PROVINCIA (id, nombre) VALUES (31, 'Lugo');
INSERT INTO PROVINCIA (id, nombre) VALUES (32, 'Madrid');
INSERT INTO PROVINCIA (id, nombre) VALUES (33, 'Málaga');
INSERT INTO PROVINCIA (id, nombre) VALUES (34, 'Melilla');
INSERT INTO PROVINCIA (id, nombre) VALUES (35, 'Murcia');
INSERT INTO PROVINCIA (id, nombre) VALUES (36, 'Navarra');
INSERT INTO PROVINCIA (id, nombre) VALUES (37, 'Orense');
INSERT INTO PROVINCIA (id, nombre) VALUES (38, 'Palencia');
INSERT INTO PROVINCIA (id, nombre) VALUES (39, 'Pontevedra');
INSERT INTO PROVINCIA (id, nombre) VALUES (40, 'Salamanca');
INSERT INTO PROVINCIA (id, nombre) VALUES (41, 'Santa Cruz de Tenerife');
INSERT INTO PROVINCIA (id, nombre) VALUES (42, 'Segovia');
INSERT INTO PROVINCIA (id, nombre) VALUES (43, 'Sevilla');
INSERT INTO PROVINCIA (id, nombre) VALUES (44, 'Soria');
INSERT INTO PROVINCIA (id, nombre) VALUES (45, 'Tarragona');
INSERT INTO PROVINCIA (id, nombre) VALUES (46, 'Teruel');
INSERT INTO PROVINCIA (id, nombre) VALUES (47, 'Toledo');
INSERT INTO PROVINCIA (id, nombre) VALUES (48, 'Valencia');
INSERT INTO PROVINCIA (id, nombre) VALUES (49, 'Valladolid');
INSERT INTO PROVINCIA (id, nombre) VALUES (50, 'Vizcaya');
INSERT INTO PROVINCIA (id, nombre) VALUES (51, 'Zamora');
INSERT INTO PROVINCIA (id, nombre) VALUES (52, 'Zaragoza');

/* Sentencias sql para rellenar de datos la tabla LOCALIDAD de Murcia*/ 
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Murcia', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Cartagena', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Lorca', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Mazarrón', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('San Javier', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('San Pedro del Pinatar', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Cieza', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Caravaca de la Cruz', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Águilas', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Mula', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Jumilla', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Puerto Lumbreras', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('La Unión', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Alhama de Murcia', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Torre-Pacheco', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Beniel', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Fuente Álamo de Murcia', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Librilla', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Lorquí', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Santomera', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Ricote', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Aledo', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Moratalla', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Roldán', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Zarandona', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Javalí Nuevo', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Javalí Viejo', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('El Palmar', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Espinardo', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('El Raal', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('San Ginés', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('Sierra de los Ríos', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('La Alberca', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('La Arboleja', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('La Murta', 35);
INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES ('La Ñora', 35);

CREATE TABLE IF NOT EXISTS DIRECCION (
	id INT auto_increment NOT NULL,
	calle varchar(100) NOT NULL,
	numero varchar(200) NOT NULL,
    codigo_postal INT,
    localidad_id INT,
	activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_DIRECCION_LOCALIDAD FOREIGN KEY (localidad_id)
        REFERENCES LOCALIDAD(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS TIPO_EMPLAZAMIENTO (
	id INT auto_increment NOT NULL,
	nombre varchar(100) NOT NULL,
	descripcion varchar(200) NOT NULL,
	activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (nombre)
);

/*Sentencias sql para rellenar de datos la tabla TIPO_EMPLAZAMIENTO */
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Estantería', 'Estructura para almacenar objetos en varios niveles.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Leja', 'Repisa o estante pequeño.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Armario', 'Mueble con puertas para almacenamiento.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Caja', 'Recipiente cerrado para almacenar objetos.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Contenedor', 'Recipiente grande para almacenar objetos.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Bodega', 'Espacio para almacenar vinos o alimentos.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Pallet', 'Estructura plana para apilar y transportar mercancías.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Vitrina', 'Mueble con puertas de cristal para exhibir objetos.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Baúl', 'Recipiente grande y resistente para almacenar.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Librero', 'Mueble para almacenar libros.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Cajón de herramientas', 'Compartimento para almacenar herramientas.');
INSERT INTO TIPO_EMPLAZAMIENTO (nombre, descripcion) VALUES ('Arcon', 'Caja grande y resistente para almacenar.');

CREATE TABLE IF NOT EXISTS EMPLAZAMIENTO (
	id INT auto_increment NOT NULL,
	nombre varchar(100) NOT NULL,
	descripcion varchar(200) NOT NULL,
    tipo_id INT NOT NULL,
	activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
     CONSTRAINT FK_EMPLAZAMIENTO_TIPO FOREIGN KEY (tipo_id)
        REFERENCES TIPO_EMPLAZAMIENTO(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ALMACEN (
	id INT auto_increment NOT NULL,
	nombre varchar(100) NOT NULL,
	descripcion varchar(200) NOT NULL,
    imagen varchar(200),
    direccion_id INT,
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (nombre),
    CONSTRAINT FK_ALMACEN_DIRECCION FOREIGN KEY (direccion_id)
        REFERENCES DIRECCION(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
