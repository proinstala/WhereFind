package io.proinstala.wherefind.api.infraestructure.data;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.api.infraestructure.data.services.DireccionServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.LocalidadServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ProvinciaServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.UserServiceImplement;

/**
 * Clase GestionPersistencia que gestiona la persistencia y proporciona servicios relacionados con los usuarios.
 */
public class GestorPersistencia {

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

    public static ILocalidadService getLocalidadService() {
        return new LocalidadServiceImplement();
    }
    
    public static IDireccionService getDireccionService() {
        return new DireccionServiceImplement();
    }
    
}
