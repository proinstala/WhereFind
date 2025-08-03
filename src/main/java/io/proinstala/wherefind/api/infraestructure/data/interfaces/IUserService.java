package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.UserDTO;
import java.sql.SQLException;

/**
 * Interfaz para el servicio de usuarios.
 */
public interface IUserService {
    /**
     * Añade un nuevo usuario.
     *
     * @param userDTO el objeto UserDTO que representa al nuevo usuario a añadir.
     * @return el objeto UserDTO del usuario añadido.
     * @throws java.lang.Exception
     */
    public UserDTO add(UserDTO userDTO) throws Exception;

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws java.lang.Exception
     */
    public boolean update(UserDTO userDTO) throws Exception;

    /**
     * Actualiza el password de un usuario existente.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws java.lang.Exception
     */
    public boolean updatePasswordUser(UserDTO userDTO) throws Exception;

    /**
     * Elimina un usuario existente.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean delete(UserDTO userDTO);

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

    
    /**
     * Genera un mensaje de error legible para el usuario basado en una excepción SQL.
     *
     * <p>Este método permite interpretar excepciones relacionadas con la violación de restricciones,
     * como claves únicas, devolviendo un mensaje más comprensible y amigable para mostrar al usuario.</p>
     *
     * @param ex la excepción {@link SQLException} capturada al interactuar con la base de datos.
     * @return un mensaje de error personalizado en función de la causa de la excepción,
     *         o un mensaje genérico si no se puede identificar el problema específico.
     */
    public String getMensajeFromSQLException(SQLException ex);


    /**
     * Obtiene los datos de un usuario existente mediante su nombre de usuario o email.
     *
     * @param id el nombre de usuario del usuario o el email.
     * @return el objeto UserDTO del usuario que coincide con el nombre de usuario o el email.
     */
    public UserDTO getUserByUserNameOrEmail(String id);


    /**
     * Obtener el número de intentos para una contraseña de recuperación dada.
     *
     * @param id identificaedor único de la solicitud
     * @return Número de intentos para la contraseña de recuperación
     */
    public boolean getRecoveryIntentos(String id);

    /**
     * Activa o desactiva un usuario de la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean activar(UserDTO userDTO);
}
