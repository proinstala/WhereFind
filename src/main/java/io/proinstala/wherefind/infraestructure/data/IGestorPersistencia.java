package io.proinstala.wherefind.infraestructure.data;

import java.util.List;

import io.proinstala.wherefind.shared.Dto.UserDto;

public interface IGestorPersistencia {

    // --------------------------------------
    // Gestion de Usuarios
    // --------------------------------------

    // Añade un nuevo usuario
    public boolean UsersAdd(UserDto usuario);

    // Actualiza los datos de un usuario existente
    public boolean UsersUpdate(UserDto usuario);

    // Elimina un usuario existente
    public boolean UsersDelete(UserDto usuario);

    // Obtiene los datos de un usuario existente
    public UserDto UsersGetUser(String userName, String password);

    // Obtiene la lista de todos los usuarios existentes
    public List<UserDto> UsersGetAll();

}
