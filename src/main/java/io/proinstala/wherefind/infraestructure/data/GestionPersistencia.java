package io.proinstala.wherefind.infraestructure.data;

import io.proinstala.wherefind.infraestructure.data.interfaces.IPoblacionService;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.infraestructure.data.services.PoblacionServiceMySql;
import io.proinstala.wherefind.infraestructure.data.services.UserServiceMySql;

/**
 * Clase GestionPersistencia que gestiona la persistencia y proporciona servicios relacionados con los usuarios.
 */
public class GestionPersistencia {

    /**
     * Obtiene una instancia del servicio de usuarios.
     *
     * @return una instancia de IUserService que implementa UserServiceMySql.
     */
    public static IUserService getUserService() {
        return new UserServiceMySql();
    }

    public static IPoblacionService getPoblacionService() {
        return new PoblacionServiceMySql();
    }

}
