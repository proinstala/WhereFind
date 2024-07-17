package io.proinstala.wherefind.infraestructure.data.services;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.*;

import io.proinstala.wherefind.infraestructure.data.interfaces.IPoblacionService;
import io.proinstala.wherefind.shared.dtos.PoblacionDTO;

public class PoblacionServiceMySql implements IPoblacionService {

    //---------------------------------------------
    // Sentencias para trabajar con mysql
    //---------------------------------------------
    // Obtiene toda la lista de poblaciones
    private static final String SQL_SELECT_ALL = "SELECT * FROM POBLACION;";

    // Añada un nueva poblacion
    private static final String SQL_INSERT_NEW = "INSERT INTO POBLACION (name, cp) VALUES(?, ?);";


    /**
     * Constructor por defecto.
     */
    public PoblacionServiceMySql()
    {
        try {
            // Necesario para que el servidor inicialice el driver de mysql
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una conexión a la base de datos MySQL.
     *
     * @return una conexión a la base de datos.
     */
    private Connection getConnection()
    {
        try {
            // Se declara el initContext
            Context initContext;

            // Se instancia el initContext
            initContext = new InitialContext();

            // Devuelve el Context
            Context envContext = (Context) initContext.lookup("java:comp/env");

            // Devuelve el DataSource
            DataSource ds = (DataSource) envContext.lookup("jdbc/WHERE_FIND_DATA");

            // Devuelve la conexión
            return ds.getConnection();
        }
        catch (NamingException | SQLException e)
        {
            e.printStackTrace();
        }

        // Deveulve nulo en caso de error
        return null;
    }

    /**
     * Convierte un ResultSet en un objeto UserDTO.
     *
     * @param resultSet el conjunto de resultados de la base de datos.
     * @return un objeto UserDTO.
     */
    private static PoblacionDTO getPoblacionFromResultSet(ResultSet resultSet)
    {
        try {
            // Crea y devuelve un UserDTO a partir de los datos de un ResultSet
            return new PoblacionDTO(
                resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("cp")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Deveulve nulo en caso de error
        return null;
    }




    @Override
    public PoblacionDTO add(PoblacionDTO poblacion) {
        // Para recoger el numero de filas afectadas
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERT_NEW);

            // Le paso los parametros al PreparedStatement
            sentencia.setString(1, poblacion.getName());
            sentencia.setString(2, poblacion.getCp());

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
        return poblacion;
    }

    @Override
    public List<PoblacionDTO> getAllPoblaciones() {
        // Se declara e instancia la lista donde se almacenarán los UserDTO
        List<PoblacionDTO> resultado = new ArrayList<>();
        try
        {
            // Se crea la conexion
            Connection conexion  = getConnection();

            // Preparo un PreparedStatement con la sentencia necesaria para saber el numero de filas
            PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECT_ALL);

            // Se ejecuta la query y nos devuelve un resultado
            ResultSet resultSet = sentencia.executeQuery();

            // Recupera la lista
            while(resultSet.next())
            {
                PoblacionDTO userActual = getPoblacionFromResultSet(resultSet);
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

        // devuelve la lista de UserDTO
        return resultado;
    }

}
