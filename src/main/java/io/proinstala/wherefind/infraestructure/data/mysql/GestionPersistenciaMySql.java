package io.proinstala.wherefind.infraestructure.data.mysql;

import io.proinstala.wherefind.infraestructure.data.IGestorPersistencia;
import io.proinstala.wherefind.shared.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GestionPersistenciaMySql implements IGestorPersistencia {
    //---------------------------------------------
    // Cadena de conexión  - Ahora está definido en src\main\webapp\META-INF\context.xml
    //---------------------------------------------
    // private static final String URL = "jdbc:mysql://localhost:3306/WhereFindData";

    //---------------------------------------------
    // Datos de conexión a la bbdd - Ahora está definido en src\main\webapp\META-INF\context.xml
    //---------------------------------------------
    //private static final String USER     = "root";
    //private static final String PASSWORD = "12345";

    //---------------------------------------------
    // Clave para codificar los passwords
    //---------------------------------------------
    private static final String KEY_SECRET_ENCODE = "|--Where-Find--|";

    //---------------------------------------------
    // Sentencias para trabajar con mysql
    //---------------------------------------------
    // Obtiene toda la lista de usuarios
    private static final String SQL_SELECT_ALL_USERS = "SELECT Id, UserName, AES_DECRYPT(Password, '" + KEY_SECRET_ENCODE + "') AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE;";

    // Obtiene un usuario en concreto que coincidan su username y su password
    private static final String SQL_SELECT_GET_USER = "SELECT Id, UserName, AES_DECRYPT(Password, '"+ KEY_SECRET_ENCODE + "') AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE AND UserName=? AND Password=AES_ENCRYPT(?,'"+ KEY_SECRET_ENCODE + "');";

    // Obtiene un usuario en concreto que coincidan su username y su password
    private static final String SQL_SELECT_GET_USER_BY_ID = "SELECT Id, UserName, AES_DECRYPT(Password, '"+ KEY_SECRET_ENCODE + "') AS Password, Rol FROM WhereFindData.Users WHERE IsDelete = FALSE AND Id=?;";

    // Añada un nuevo user
    private static final String SQL_INSERT_NEW_USER = "INSERT INTO WhereFindData.Users (UserName, Password, Rol, IsDelete) VALUES(?, AES_ENCRYPT(?,'"+ KEY_SECRET_ENCODE + "'), ?, 0);";

    // Actualiza el rol y el password a un usuario
    private static final String SQL_UPDATE_USER = "UPDATE WhereFindData.Users SET Rol=?, Password=AES_ENCRYPT(?, '"+ KEY_SECRET_ENCODE + "') WHERE Id=?;";

    // Marca a un usuario como eliminado
    private static final String SQL_DELETE_USER = "UPDATE WhereFindData.Users SET IsDelete=True WHERE Id=?;";



    public GestionPersistenciaMySql()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection()
    {
        try {
            Context initContext;
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/WhereFindData");
            return ds.getConnection();
        }
        catch (NamingException | SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static UserDTO getUserFromResultSet(ResultSet resultSet)
    {
        try {
            return new UserDTO(
                resultSet.getInt("Id"),
                    resultSet.getString("UserName"),
                    resultSet.getString("Password"),
                    resultSet.getString("Rol")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public UserDTO usersAdd(UserDTO usuario) {
        // Para recoger el numero de filas afectadas
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERT_NEW_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, usuario.getUserName());
            sentencia.setString(2, usuario.getPassword());
            sentencia.setString(3, usuario.getRol());

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

        return usersGetUser(usuario.getUserName(), usuario.getPassword());
    }

    @Override
    public boolean usersUpdate(UserDTO usuario) {

        // Para recoger el numero de filas afectadas
        int rowAfectadas = 0;
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_UPDATE_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, usuario.getRol());
            sentencia.setString(2, usuario.getPassword());
            sentencia.setInt(3, usuario.getId());

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

    @Override
    public boolean usersDelete(UserDTO usuario) {
        // Para recoger el numero de filas afectadas
        int rowAfectadas = 0;
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_DELETE_USER);

            // Le paso los parametros al PreparedStatement
            sentencia.setInt(1, usuario.getId());

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

    @Override
    public UserDTO usersGetUser(String userName, String password) {

        UserDTO resultado = null;
        try
        {
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
                resultado = getUserFromResultSet(resultSet);
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

        return resultado;
    }

    @Override
    public List<UserDTO> usersGetAll() {
        List<UserDTO> resultado = new ArrayList<>();
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
                UserDTO userActual = getUserFromResultSet(resultSet);
                if (userActual != null) {
                    resultado.add(userActual);
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

        return resultado;
    }

    @Override
    public UserDTO usersGetUserById(int id) {

        UserDTO resultado = null;
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
                resultado = getUserFromResultSet(resultSet);
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

        return resultado;
    }
}
