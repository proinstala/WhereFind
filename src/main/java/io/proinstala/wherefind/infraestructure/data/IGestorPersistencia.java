package io.proinstala.wherefind.infraestructure.data;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.UserDTO;

public interface IGestorPersistencia {

    // --------------------------------------
    // Gestion de Usuarios
    // --------------------------------------

    // Añade un nuevo usuario
    public UserDTO UsersAdd(UserDTO usuario);

    // Actualiza los datos de un usuario existente
    public boolean UsersUpdate(UserDTO usuario);

    // Elimina un usuario existente
    public boolean UsersDelete(UserDTO usuario);

    // Obtiene los datos de un usuario existente con su Id
    public UserDTO UsersGetUserById(int id);

    // Obtiene los datos de un usuario existente
    public UserDTO UsersGetUser(String userName, String password);

    // Obtiene la lista de todos los usuarios existentes
    public List<UserDTO> UsersGetAll();

}
