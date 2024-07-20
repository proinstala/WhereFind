package io.proinstala.wherefind.infraestructure.data.services;

import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
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

    // Actualiza el rol y el password a un usuario
    private static final String SQL_UPDATE_USER = "UPDATE USER SET rol=?, password=ENCRYPT_DATA_BASE64(?) WHERE id=?;";

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
     */
    @Override
    public UserDTO add(UserDTO userDTO) {
        // Para recoger el numero de filas afectadas
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERT_NEW_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, userDTO.getUserName());
            sentencia.setString(2, userDTO.getPassword());
            sentencia.setString(3, userDTO.getRol());
            sentencia.setString(4, userDTO.getNombre());
            sentencia.setString(5, userDTO.getApellidos());
            sentencia.setString(6, userDTO.getEmail());
            sentencia.setString(7, userDTO.getImagen());

            // Ejecuto la sentencia preparada
            sentencia.executeUpdate();

            // Cerramos todo lo que hemos usado
            sentencia.close();
            conexion.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Vuelve a conectarse a mysql para devolver el usuario recién creado
        return getUser(userDTO.getUserName(), userDTO.getPassword());
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param userDTO el objeto UserDTO que representa al usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    @Override
    public boolean update(UserDTO userDTO) {

        // Para recoger el numero de filas afectadas
        int rowAfectadas = 0;
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_UPDATE_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, userDTO.getRol());
            sentencia.setString(2, userDTO.getPassword());
            sentencia.setInt(3, userDTO.getId());

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
}
