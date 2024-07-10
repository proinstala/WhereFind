package io.proinstala.wherefind.infraestructure.data.mysql;

import io.proinstala.wherefind.infraestructure.data.IGestorPersistencia;
import io.proinstala.wherefind.shared.Dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class GestionPersistenciaMySql implements IGestorPersistencia {
    //---------------------------------------------
    // Cadena de conexión
    //---------------------------------------------
    private static final String URL = "jdbc:mysql://localhost:3306/WhereFindData";
    // ?useSSL=false

    //---------------------------------------------
    // Datos de conexión a la bbdd
    //---------------------------------------------
    private static final String USER     = "root";
    private static final String PASSWORD = "12345";

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


    public GestionPersistenciaMySql()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static UserDto getUserFromResultSet(ResultSet resultSet)
    {
        try {
            UserDto resultado = new UserDto(
                    resultSet.getInt("Id"),
                    resultSet.getString("UserName"),
                    resultSet.getString("Password"),
                    resultSet.getString("Rol")
            );

            return resultado;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean UsersAdd(UserDto usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean UsersUpdate(UserDto usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean UsersDelete(UserDto usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public UserDto UsersGetUser(String userName, String password) {

        UserDto resultado = null;
        try
        {
            // Se crea la conexion
            Connection conexion  = DriverManager.getConnection(URL, USER, PASSWORD);

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
    public List<UserDto> UsersGetAll() {
        List<UserDto> resultado = new ArrayList<UserDto>();
        try
        {
            // Se crea la conexion
            Connection conexion  = DriverManager.getConnection(URL, USER, PASSWORD);

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECT_ALL_USERS);

            // Se ejecuta la query y nos devuelve un resultado
            ResultSet resultSet = sentencia.executeQuery();

            // Recupera la lista
            while(resultSet.next())
            {
                UserDto userActual = getUserFromResultSet(resultSet);
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
    public UserDto UsersGetUserById(int id) {

        UserDto resultado = null;
        try
        {
            // Se crea la conexion
            Connection conexion  = DriverManager.getConnection(URL, USER, PASSWORD);

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
