package io.proinstala.wherefind.api.infraestructure.data;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.api.infraestructure.data.services.ProvinciaServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.UserServiceImplement;

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
        return new UserServiceImplement();
    }
    
    public static IProvinciaService getProvinciaService() {
        return new ProvinciaServiceImplement();
    }
}
