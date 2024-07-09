-- Crea la base de datos
CREATE DATABASE IF NOT EXISTS `WhereFindData` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

-- Crea la tabla Users
CREATE TABLE IF NOT EXISTS WhereFindData.Users (
	Id INT auto_increment NOT NULL,
	UserName varchar(100) NOT NULL,
	Password BLOB NOT NULL,
    Rol varchar(100) NOT NULL,
	IsDelete BOOL DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_UserName UNIQUE (UserName)
)
ENGINE=MyISAM
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

-- Se crea el index en la columna Username para un mejor rendimiento
CREATE INDEX Users_UserName_IDX USING BTREE ON WhereFindData.Users (UserName);

-- Crea un usuarios de prueba
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('david', AES_ENCRYPT('123','|--Where-Find--|'), 'Admin', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('juanma', AES_ENCRYPT('123','|--Where-Find--|'), 'Admin', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('user_normal', AES_ENCRYPT('123','|--Where-Find--|'), 'User', 0);
INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES('otro_user', AES_ENCRYPT('123','|--Where-Find--|'), 'User', 0);

-- Listar todos los usuarios
SELECT Id, UserName, AES_DECRYPT(Password, '|--Where-Find--|') AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE;

-- Obtener un usuario
SELECT Id, UserName, AES_DECRYPT(Password, '|--Where-Find--|') AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE AND UserName='david' AND Password=AES_ENCRYPT('123','|--Where-Find--|');

-- Eliminar el usuario (4) otro_user
UPDATE WhereFindData.Users SET IsDelete=True WHERE Id=4;

-- Actualiza los datos del usuario user_normal, en este ejemplo solo el password
UPDATE WhereFindData.Users SET Password=AES_ENCRYPT('321','|--Where-Find--|') WHERE Id=3;

-- Muestra todos los usuarios incluso los eliminados
SELECT Id, UserName, AES_DECRYPT(Password, '|--Where-Find--|') AS Password, Rol, IsDelete  FROM WhereFindData.Users;
