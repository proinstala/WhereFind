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
     * <p>Este método devuelve una implementación de {@link IUserService}, en este caso, 
     * una instancia de {@link UserServiceImplement}, que maneja la lógica relacionada con 
     * los usuarios.</p>
     *
     * @return una instancia de {@link IUserService}.
     */
    public static IUserService getUserService() {
        return new UserServiceImplement();
    }

    /**
     * Obtiene una instancia del servicio de provincias.
     *
     * <p>Este método devuelve una implementación de {@link IProvinciaService}, en este caso, 
     * una instancia de {@link ProvinciaServiceImplement}, que maneja la lógica relacionada con 
     * las provincias.</p>
     *
     * @return una instancia de {@link IProvinciaService}.
     */
    public static IProvinciaService getProvinciaService() {
        return new ProvinciaServiceImplement();
    }

    /**
     * Obtiene una instancia del servicio de localidades.
     *
     * <p>Este método devuelve una implementación de {@link ILocalidadService}, en este caso, 
     * una instancia de {@link LocalidadServiceImplement}, que maneja la lógica relacionada con 
     * las localidades.</p>
     *
     * @return una instancia de {@link ILocalidadService}.
     */
    public static ILocalidadService getLocalidadService() {
        return new LocalidadServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de direcciones.
     *
     * <p>Este método devuelve una implementación de {@link IDireccionService}, en este caso, 
     * una instancia de {@link DireccionServiceImplement}, que maneja la lógica relacionada con 
     * las direcciones.</p>
     *
     * @return una instancia de {@link IDireccionService}.
     */
    public static IDireccionService getDireccionService() {
        return new DireccionServiceImplement();
    }
    
}
