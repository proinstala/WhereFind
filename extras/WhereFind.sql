-- Crea la base de datos
CREATE DATABASE IF NOT EXISTS `WhereFindData`;

use `WhereFindData`;


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



-- Crea la tabla Users
CREATE TABLE IF NOT EXISTS WhereFindData.Users (
	Id INT auto_increment NOT NULL,
	UserName varchar(100) NOT NULL,
	Password TEXT NOT NULL,
    Rol varchar(100) NOT NULL,
	IsDelete BOOL DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_UserName UNIQUE (UserName)
);

-- Se crea el index en la columna Username para un mejor rendimiento
CREATE INDEX Users_UserName_IDX USING BTREE ON WhereFindData.Users (UserName);

-- Crea un usuarios de prueba
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('david', ENCRYPT_DATA_BASE64('123'), 'Admin', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('juanma', ENCRYPT_DATA_BASE64('123'), 'Admin', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('user_normal', ENCRYPT_DATA_BASE64('123'), 'User', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('otro_user', ENCRYPT_DATA_BASE64('123'), 'User', 0);

-- Listar todos los usuarios
SELECT Id, UserName, DECRYPT_DATA_BASE64(Password) AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE;

-- Obtener un usuario
SELECT Id, UserName, DECRYPT_DATA_BASE64(Password) AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE AND UserName='david' AND Password=ENCRYPT_DATA_BASE64('123');

-- Eliminar el usuario (4) otro_user
UPDATE WhereFindData.Users SET IsDelete=True WHERE Id=4;

-- Actualiza los datos del usuario user_normal, en este ejemplo solo el password
UPDATE WhereFindData.Users SET Password=ENCRYPT_DATA_BASE64('321') WHERE Id=3;

-- Muestra todos los usuarios incluso los eliminados
SELECT Id, UserName, DECRYPT_DATA_BASE64(Password) AS Password, Rol, IsDelete  FROM WhereFindData.Users;


