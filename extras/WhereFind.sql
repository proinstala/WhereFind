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
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (user_name),
    CONSTRAINT UC_EMAIL UNIQUE (email)
);

-- Se crea el index en la columna user_name para un mejor rendimiento
CREATE INDEX USER_NOMBRE_IDX USING BTREE ON USER (user_name);

-- Crea un usuarios de prueba
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('david', ENCRYPT_DATA_BASE64('123'), 'Admin', 1, 'David', 'Jiménez Alonso', 'david@email.es');
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('juanma', ENCRYPT_DATA_BASE64('123'), 'Admin', 1, 'Juan Manuel', 'Soltero Sánchez', 'juanma@email.es');
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('user_normal', ENCRYPT_DATA_BASE64('123'), 'User', 1, 'User', 'Normal', 'user_normal@email.es');
INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('otro_user', ENCRYPT_DATA_BASE64('123'), 'User', 1, 'Otro', 'User', 'otro_user@email.es');

-- Listar todos los usuarios
SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, nombre, apellidos, email FROM USER WHERE activo = TRUE;

-- Obtener un usuario
SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, nombre, apellidos, email FROM USER WHERE activo = TRUE AND user_name='david' AND password=ENCRYPT_DATA_BASE64('123');

-- Eliminar el usuario (4) otro_user
UPDATE USER SET activo=FALSE WHERE id=4;

-- Actualiza los datos del usuario user_normal, en este ejemplo solo el password
UPDATE USER SET password=ENCRYPT_DATA_BASE64('321') WHERE id=3;

-- Muestra todos los usuarios incluso los eliminados
SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, activo, nombre, apellidos, email FROM USER;



-- Agregar un usuario existente cambiando en el user name y en el email mayúsculas y minúsculas
-- INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email) VALUES('USER_Normal', ENCRYPT_DATA_BASE64('123'), 'User', 1, 'User', 'Normal', 'useR_Normal@email.es');


































use `WHERE_FIND_DATA`;


-- Elimina la tabla POBLACION
DROP TABLE IF EXISTS WHERE_FIND_DATA.`POBLACION`;

-- Crea la tabla POBLACION
CREATE TABLE IF NOT EXISTS POBLACION (
	id INT auto_increment NOT NULL,
	name varchar(200) NOT null,
    PRIMARY KEY (id),
    CONSTRAINT UC_NOMBRE UNIQUE (name)
);

-- Se crea el index en la columna name para un mejor rendimiento
CREATE INDEX POBLACION_NOMBRE_IDX USING BTREE ON POBLACION (name);

-- Crea unas poblaciones de prueba
INSERT INTO POBLACION (name) VALUES('Madrid');
INSERT INTO POBLACION (name) VALUES('Murcia');
INSERT INTO POBLACION (name) VALUES('Sevilla');
INSERT INTO POBLACION (name) VALUES('Granada');


-- Listar todas los poblaciones
SELECT * FROM POBLACION;

-- Obtener una poblacion por nombre
SELECT * FROM POBLACION WHERE name='Murcia';

-- Obtener una poblacion por id
SELECT * FROM POBLACION WHERE id=3;
