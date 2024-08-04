package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * Clase UserServiceMySql que gestiona las operaciones relacionadas con usuarios en una base de datos MySQL.
 */
public class UserServiceImplement extends BaseMySql implements IUserService {
    //---------------------------------------------
    // Sentencias para trabajar con mysql
    //---------------------------------------------
    // Obtiene toda la lista de usuarios
    private static final String SQL_SELECT_ALL_USERS = "SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, nombre, apellidos, email, imagen FROM USER WHERE activo = TRUE;";

    // Obtiene un usuario en concreto que coincidan su user_name y su password
    private static final String SQL_SELECT_GET_USER = "SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, nombre, apellidos, email, imagen FROM USER WHERE activo = TRUE AND user_name=? AND password=ENCRYPT_DATA_BASE64(?);";

    // Obtiene un usuario en concreto que coincidan su user_name y su password
    private static final String SQL_SELECT_GET_USER_BY_ID = "SELECT id, user_name, DECRYPT_DATA_BASE64(password) AS password, rol, nombre, apellidos, email, imagen FROM USER WHERE activo = TRUE AND id=?;";

    // Añada un nuevo user
    private static final String SQL_INSERT_NEW_USER = "INSERT INTO USER (user_name, password, rol, activo, nombre, apellidos, email, imagen) VALUES(?, ENCRYPT_DATA_BASE64(?), ?, 1, ?, ?, ?, ?);";

    // Actualiza el rol a un usuario
    private static final String SQL_UPDATE_USER_ROL = "UPDATE USER SET rol=? WHERE id=?;";

    // Actualiza el password a un usuario
    private static final String SQL_UPDATE_USER_PASSWORD = "UPDATE USER SET password=ENCRYPT_DATA_BASE64(?) WHERE id=?;";

    // Actualiza los datos generales a un usuario
    private static final String SQL_UPDATE_USER = "UPDATE USER SET user_name=?, nombre=?, apellidos=?, email=?, imagen=? WHERE id=?;";

    // Marca a un usuario como eliminado
    private static final String SQL_DELETE_USER = "UPDATE USER SET activo=false WHERE id=?;";

    /**
     * Convierte un ResultSet en un objeto UserDTO.
     *
     * @param resultSet el conjunto de resultados de la base de datos.
     * @return un objeto UserDTO.
     */
    private static UserDTO getUserFromResultSet(ResultSet resultSet)
    {
        try {
            // Crea y devuelve un UserDTO a partir de los datos de un ResultSet
            return new UserDTO(
                resultSet.getInt("id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    resultSet.getString("rol"),
                    resultSet.getString("nombre"),
                    resultSet.getString("apellidos"),
                    resultSet.getString("email"),
                    resultSet.getString("imagen")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Deveulve nulo en caso de error
        return null;
    }


    /**
     * Añade un nuevo usuario a la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al nuevo usuario.
     * @return el objeto UserDTO añadido.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    @Override
    public UserDTO add(UserDTO userDTO) throws SQLException {
        // Usar try-with-resources para manejar automáticamente la liberación de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT_NEW_USER)) {

            // Asignar parámetros al PreparedStatement
            ps.setString(1, userDTO.getUserName());
            ps.setString(2, userDTO.getPassword());
            ps.setString(3, userDTO.getRol());
            ps.setString(4, userDTO.getNombre());
            ps.setString(5, userDTO.getApellidos());
            ps.setString(6, userDTO.getEmail());
            ps.setString(7, userDTO.getImagen());

            // Ejecutar la sentencia preparada
            ps.executeUpdate();
        }

        // Vuelve a conectarse a la base de datos para devolver el usuario recién creado
        return getUser(userDTO.getUserName(), userDTO.getPassword());
    }


    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean update(UserDTO userDTO) throws SQLException {
        // Contador de filas afectadas
        int rowsAffected = 0;

        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_USER)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setString(1, userDTO.getUserName());
            ps.setString(2, userDTO.getNombre());
            ps.setString(3, userDTO.getApellidos());
            ps.setString(4, userDTO.getEmail());
            ps.setString(5, userDTO.getImagen());
            ps.setInt(6, userDTO.getId());

            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        }

        // Retornar si se afectaron más de 0 filas
        return rowsAffected > 0;
    }
    
    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updatePasswordUser(UserDTO userDTO) throws SQLException {
        // Contador de filas afectadas
        int rowsAffected = 0;

        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_USER_PASSWORD)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setString(1, userDTO.getPassword());
            ps.setInt(2, userDTO.getId());

            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        }

        // Retornar si se afectaron más de 0 filas
        return rowsAffected > 0;
    }


    /**
     * Elimina un usuario de la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean delete(UserDTO userDTO) {
        // Para recoger el numero de filas afectadas
        int rowAfectadas = 0;
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_DELETE_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setInt(1, userDTO.getId());

            // Ejecuto la sentencia preparada
            rowAfectadas = sentencia.executeUpdate();

            // Cerramos todo lo que hemos usado
            sentencia.close();
            conexion.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rowAfectadas > 0;
    }

    /**
     * Obtiene un usuario de la base de datos basado en su nombre de usuario y contraseña.
     *
     * @param userName el nombre de usuario.
     * @param password la contraseña del usuario.
     * @return el objeto UserDTO correspondiente al usuario, o null si no se encuentra.
     */
    @Override
    public UserDTO getUser(String userName, String password) {

        UserDTO userDTO = null;
        try
        {
            System.out.println("username = " + userName);
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECT_GET_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, userName);
            sentencia.setString(2, password);

            // Se ejecuta la query y nos devuelve un resultado
            ResultSet resultSet = sentencia.executeQuery();

            // Recupera la lista
            if (resultSet.next())
            {
                userDTO = getUserFromResultSet(resultSet);
            }

            // Cerramos todo lo que hemos usado
            resultSet.close();
            sentencia.close();
            conexion.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return userDTO;
    }

    /**
     * Obtiene una lista de todos los usuarios de la base de datos.
     *
     * @return una lista de objetos UserDTO que representan a todos los usuarios.
     */
    @Override
    public List<UserDTO> getAllUsers() {
        // Se declara e instancia la lista donde se almacenarán los UserDTO
        List<UserDTO> listUserDTO = new ArrayList<>();
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECT_ALL_USERS);

            // Se ejecuta la query y nos devuelve un resultado
            ResultSet resultSet = sentencia.executeQuery();

            // Recupera la lista
            while(resultSet.next())
            {
                UserDTO userDTO = getUserFromResultSet(resultSet);
                if (userDTO != null) {
                    listUserDTO.add(userDTO);
                }
            }

            // Cerramos todo lo que hemos usado
            resultSet.close();
            sentencia.close();
            conexion.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // devuelve la lista de UserDTO
        return listUserDTO;
    }

    /**
     * Obtiene un usuario de la base de datos basado en su ID.
     *
     * @param id el ID del usuario.
     * @return el objeto UserDTO correspondiente al usuario, o null si no se encuentra.
     */
    @Override
    public UserDTO getUserById(int id) {

        // Declada el resultado
        UserDTO userDTO = null;
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECT_GET_USER_BY_ID);

            // Le paso los parametros al PreparedStatement
            sentencia.setInt(1, id);

            // Se ejecuta la query y nos devuelve un resultado
            ResultSet resultSet = sentencia.executeQuery();

            // Recupera la lista
            if (resultSet.next())
            {
                userDTO = getUserFromResultSet(resultSet);
            }

            // Cerramos todo lo que hemos usado
            resultSet.close();
            sentencia.close();
            conexion.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Devuelve el resultado
        return userDTO;
    }

    /**
     * Verifica si el estado obtenido de la excepción es igual a un estado específico.
     *
     * Este método verifica si el estado obtenido de la excepción proporcionada
     * es igual a un estado específico. Devuelve {@code true} si los estados son iguales,
     * y {@code false} en caso contrario.
     *
     * @param ex la excepción de la cual se obtiene el estado.
     * @return {@code true} si el estado obtenido de la excepción es igual al estado específico,
     *         {@code false} en caso contrario.
     */
    @Override
    public boolean isGetStateEqualFromException(Exception ex)
    {
        return ((SQLException)ex).getSQLState().equals("23000");
    }
}
