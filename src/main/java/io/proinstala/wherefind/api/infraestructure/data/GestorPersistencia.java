package io.proinstala.wherefind.api.infraestructure.data;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IAlmacenService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IArticuloService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IMarcaService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProveedorService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IPuestoTrabajoService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ITipoEmplazamientoService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.api.infraestructure.data.services.AlmacenServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ArticuloServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ContactoServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.DireccionServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.EmplazamientoServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.LocalidadServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.MarcaServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ProveedorServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.ProvinciaServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.PuestoTrabajoServiceImplement;
import io.proinstala.wherefind.api.infraestructure.data.services.TipoEmplazamientoServiceImplement;
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
    
    
    /**
     * Obtiene una instancia del servicio de contactos.
     *
     * <p>Este método devuelve una implementación de {@link IContactoService}, en este caso, 
     * una instancia de {@link ContactoServiceImplement}, que maneja la lógica relacionada con 
     * los contactos de proveedor.</p>
     *
     * @return una instancia de {@link IContactoService}.
     */
    public static IContactoService getContactoService() {
        return new ContactoServiceImplement();
    }
    
    
    /**
     * Obtiene una instancia del servicio de puestos de trabajo.
     *
     * <p>Este método devuelve una implementación de {@link IPuestoTrabajoService}, en este caso, 
     * una instancia de {@link PuestoTrabajoServiceImplement}, que maneja la lógica relacionada con 
     * los puestos de trabajo de los contactos.</p>
     *
     * @return una instancia de {@link IPuestoTrabajoService}.
     */
    public static IPuestoTrabajoService getPuestoTrabajoService() {
        return new PuestoTrabajoServiceImplement();
    }
    
    
    /**
     * Obtiene una instancia del servicio de proveedores.
     *
     * <p>Este método devuelve una implementación de {@link IProveedorService}, en este caso, 
     * una instancia de {@link ProveedorServiceImplement}, que maneja la lógica relacionada con 
     * los proveedores.</p>
     *
     * @return una instancia de {@link IProveedorService}.
     */
    public static IProveedorService getProveedorService() {
        return new ProveedorServiceImplement();
    }
    
    
    /**
     * Obtiene una instancia del servicio de tipos de emplazamiento.
     *
     * <p>Este método devuelve una implementación de {@link ITipoEmplazamientoService}, en este caso, 
     * una instancia de {@link TipoEmplazamientoServiceImplement}, que maneja la lógica relacionada con 
     * los tipos de emplazamientos.</p>
     *
     * @return una instancia de {@link ITipoEmplazamientoService}.
     */
    public static ITipoEmplazamientoService getTipoEmplazamientoService() {
        return new TipoEmplazamientoServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de emplazamientos.
     *
     * <p>Este método devuelve una implementación de {@link IEmplazamientoService}, en este caso, 
     * una instancia de {@link EmplazamientoServiceImplement}, que maneja la lógica relacionada con 
     * los emplazamientos.</p>
     *
     * @return una instancia de {@link IEmplazamientoService}.
     */
    public static IEmplazamientoService getEmplazamientoService() {
        return new EmplazamientoServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de almacenes.
     *
     * <p>Este método devuelve una implementación de {@link IAlmacenService}, en este caso, 
     * una instancia de {@link AlmacenServiceImplement}, que maneja la lógica relacionada con 
     * los almacenes.</p>
     *
     * @return una instancia de {@link IAlmacenService}.
     */
    public static IAlmacenService getAlmacenService() {
        return new AlmacenServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de marcas.
     *
     * <p>Este método devuelve una implementación de {@link IMarcaService}, en este caso, 
     * una instancia de {@link MarcaServiceImplement}, que maneja la lógica relacionada con 
     * las marcas.</p>
     *
     * @return una instancia de {@link IMarcaService}.
     */
    public static IMarcaService getMarcaService() {
        return new MarcaServiceImplement();
    }
    
    /**
     * Obtiene una instancia del servicio de artículos.
     *
     * <p>Este método devuelve una implementación de {@link IArticuloService}, en este caso, 
     * una instancia de {@link ArticuloServiceImplement}, que maneja la lógica relacionada con 
     * los artículos.</p>
     *
     * @return una instancia de {@link IArticuloService}.
     */
    public static IArticuloService getArticuloService() {
        return new ArticuloServiceImplement();
    }
    
}
