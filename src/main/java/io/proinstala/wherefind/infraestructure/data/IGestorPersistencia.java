package io.proinstala.wherefind.infraestructure.data;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.UserDTO;

public interface IGestorPersistencia {

    // --------------------------------------
    // Gestion de Usuarios
    // --------------------------------------

    // Añade un nuevo usuario
    public UserDTO usersAdd(UserDTO usuario);

    // Actualiza los datos de un usuario existente
    public boolean usersUpdate(UserDTO usuario);

    // Elimina un usuario existente
    public boolean usersDelete(UserDTO usuario);

    // Obtiene los datos de un usuario existente con su Id
    public UserDTO usersGetUserById(int id);

    // Obtiene los datos de un usuario existente
    public UserDTO usersGetUser(String userName, String password);

    // Obtiene la lista de todos los usuarios existentes
    public List<UserDTO> usersGetAll();

}
