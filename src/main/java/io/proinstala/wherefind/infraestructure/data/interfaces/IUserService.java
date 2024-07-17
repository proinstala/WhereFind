package io.proinstala.wherefind.infraestructure.data.interfaces;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.UserDTO;

/**
 * Interfaz para el servicio de usuarios.
 */
public interface IUserService {
    /**
     * Añade un nuevo usuario.
     *
     * @param usuario el objeto UserDTO que representa al nuevo usuario a añadir.
     * @return el objeto UserDTO del usuario añadido.
     */
    public UserDTO add(UserDTO usuario);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean update(UserDTO usuario);

    /**
     * Elimina un usuario existente.
     *
     * @param usuario el objeto UserDTO que representa al usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean delete(UserDTO usuario);

    /**
     * Obtiene los datos de un usuario existente mediante su Id.
     *
     * @param id el identificador único del usuario.
     * @return el objeto UserDTO del usuario con el Id especificado, o null si no se encuentra.
     */
    public UserDTO getUserById(int id);

    /**
     * Obtiene los datos de un usuario existente mediante su nombre de usuario y contraseña.
     *
     * @param userName el nombre de usuario del usuario.
     * @param password la contraseña del usuario.
     * @return el objeto UserDTO del usuario que coincide con el nombre de usuario y contraseña especificados, o null si no se encuentra.
     */
    public UserDTO getUser(String userName, String password);

    /**
     * Obtiene la lista de todos los usuarios existentes.
     *
     * @return una lista de objetos UserDTO que representan a todos los usuarios.
     */
    public List<UserDTO> getAllUsers();
}
