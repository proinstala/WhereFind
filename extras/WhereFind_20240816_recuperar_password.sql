use `WHERE_FIND_DATA`;

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

-- Se crea el index en la columna hash para un mejor rendimiento
CREATE INDEX RECOVERY_HASH_IDX USING BTREE ON RECOVERY (hash);



-- Ejemplo de como usarlo
SELECT RECOVERY_GET_INTENTOS('un_hash_cualquiera') AS intentos;

