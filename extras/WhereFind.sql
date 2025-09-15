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
    user_name VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    rol VARCHAR(100) NOT NULL,
    activo BOOL DEFAULT TRUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    imagen MEDIUMTEXT,
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (user_name),
    CONSTRAINT UC_EMAIL UNIQUE (email)
);


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
    hash VARCHAR(100) NOT NULL,
    intentos INT DEFAULT 1 NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_HASH UNIQUE (hash)
);


CREATE TABLE IF NOT EXISTS PROVINCIA (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS LOCALIDAD (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    provincia_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_LOCALIDAD_PROVINCIA FOREIGN KEY (provincia_id)
        REFERENCES PROVINCIA(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DIRECCION (
    id INT auto_increment NOT NULL,
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(6),
    codigo_postal INT,
    localidad_id INT NOT NULL, 
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_DIRECCION_LOCALIDAD FOREIGN KEY (localidad_id)
        REFERENCES LOCALIDAD(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS TIPO_EMPLAZAMIENTO (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_TIPOEMP_NOMBRE UNIQUE (nombre)
);


CREATE TABLE IF NOT EXISTS ALMACEN (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    direccion_id INT,
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_ALM_NOMBRE UNIQUE (nombre),
    CONSTRAINT FK_ALMACEN_DIRECCION FOREIGN KEY (direccion_id)
        REFERENCES DIRECCION(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS EMPLAZAMIENTO (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion varchar(200) NOT NULL,
    tipo_id INT NOT NULL,
    almacen_id INT NOT NULL,
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_EMPLAZAMIENTO_TIPO FOREIGN KEY (tipo_id)
        REFERENCES TIPO_EMPLAZAMIENTO(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT FK_EMPLAZAMIENTO_ALMACEN FOREIGN KEY (almacen_id)
        REFERENCES ALMACEN(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS PROVEEDOR (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    pagina_web VARCHAR(100),
    imagen MEDIUMTEXT,
    activo BOOL DEFAULT TRUE NOT NULL,
    direccion_id INT,
    PRIMARY KEY (id),
    CONSTRAINT UC_PROV_NOMBRE UNIQUE (nombre),
    CONSTRAINT FK_PROVEEDOR_DIRECCION FOREIGN KEY (direccion_id)
        REFERENCES DIRECCION(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS PUESTO_TRABAJO (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS CONTACTO (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    puesto_id INT,
    telefono VARCHAR(20),
    email VARCHAR(200),
    activo BOOL DEFAULT TRUE NOT NULL,
    proveedor_id INT,
    PRIMARY KEY (id),
    CONSTRAINT FK_CONTACTO_PROVEEDOR FOREIGN KEY (proveedor_id)
        REFERENCES PROVEEDOR(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT FK_CONTACTO_PUESTO FOREIGN KEY (puesto_id)
        REFERENCES PUESTO_TRABAJO(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS MARCA (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    imagen MEDIUMTEXT,
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS ARTICULO (
    id INT auto_increment NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    referencia VARCHAR(100) NOT NULL,
    marca_id INT,
    modelo VARCHAR(100),
    stock_minimo INT,
    imagen MEDIUMTEXT,
    activo BOOL DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_ART_MARCA FOREIGN KEY (marca_id)
        REFERENCES MARCA(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ARTICULO_PROVEEDOR (
    id INT auto_increment NOT NULL,
    articulo_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    precio DOUBLE NOT NULL,
    fecha_precio DATE NOT NULL,
    disponible BOOL NOT NULL,
    fecha_no_disponible DATE,
    PRIMARY KEY (id),
    CONSTRAINT FK_ART_PRO_ARTICULO FOREIGN KEY (articulo_id)
        REFERENCES ARTICULO(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT FK_ART_PROV_PROVEEDOR FOREIGN KEY (proveedor_id)
        REFERENCES PROVEEDOR(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT UQ_ART_PROV_ARTPROV UNIQUE (articulo_id, proveedor_id)
);


CREATE TABLE IF NOT EXISTS EXISTENCIA (
    id INT auto_increment NOT NULL,
    articulo_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    emplazamiento_id INT NOT NULL,
    precio DOUBLE NOT NULL,
    fecha_compra DATE,
    comprador VARCHAR(100),
    disponible BOOL NOT NULL,
    fecha_no_disponible DATE,
    PRIMARY KEY (id),
    CONSTRAINT FK_EXIST_ARTICULO FOREIGN KEY (articulo_id)
        REFERENCES ARTICULO(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT FK_EXIST_PROVEEDOR FOREIGN KEY (proveedor_id)
        REFERENCES PROVEEDOR(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT FK_EXIST_EMPLAZAMIENTO FOREIGN KEY (emplazamiento_id)
        REFERENCES EMPLAZAMIENTO(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


