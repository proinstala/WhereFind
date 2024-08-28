package io.proinstala.wherefind.api.infraestructure.data;

//Importación de las interfaces de servicio
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;

//Importación de las implementaciones de servicio
import io.proinstala.wherefind.api.infraestructure.data.services.DireccionServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.LocalidadServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ProvinciaServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.UserServiceImplement;

/**
 * Clase GestionPersistencia que centraliza la gestión de la persistencia y proporciona acceso a los servicios necesarios 
 * en la capa de datos de la aplicación.
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
    
    /**
     * Obtiene una instancia del servicio de provincias.
     *
     * @return una instancia de IProvinciaService que implementa ProvinciaServiceMySql.
     */
    public static IProvinciaService getProvinciaService() {
        return new ProvinciaServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de localidades.
     *
     * @return una instancia de ILocalidadService que implementa LocalidadServiceMySql.
     */
    public static ILocalidadService getLocalidadService() {
        return new LocalidadServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de direcciones.
     *
     * @return una instancia de IDireccionService que implementa DireccionServiceMySql.
     */
    public static IDireccionService getDireccionService() {
        return new DireccionServiceImplement();
    }
}
