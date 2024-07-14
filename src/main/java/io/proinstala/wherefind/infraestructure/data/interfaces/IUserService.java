package io.proinstala.wherefind.infraestructure.data.interfaces;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.UserDTO;

public interface IUserService {

    // Añade un nuevo usuario
    public UserDTO add(UserDTO usuario);

    // Actualiza los datos de un usuario existente
    public boolean update(UserDTO usuario);

    // Elimina un usuario existente
    public boolean delete(UserDTO usuario);

    // Obtiene los datos de un usuario existente con su Id
    public UserDTO getUserById(int id);

    // Obtiene los datos de un usuario existente
    public UserDTO getUser(String userName, String password);

    // Obtiene la lista de todos los usuarios existentes
    public List<UserDTO> getAllUsers();
}
