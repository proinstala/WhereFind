package io.proinstala.wherefind.api.infraestructure.data.services;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class BaseMySql {

    /**
     * Constructor por defecto.
     */
    public BaseMySql()
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
    protected Connection getConnection()
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

}
