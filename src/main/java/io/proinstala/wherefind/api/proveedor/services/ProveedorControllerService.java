
package io.proinstala.wherefind.api.proveedor.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProveedorService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con proveedores.
 * 
 * <p>Esta clase proporciona métodos para obtener, buscar y actualizar proveedores en la base de datos.</p>
 * 
 * <p>Extiende {@link BaseService} y utiliza {@link IProveedorService} para interactuar con la base de datos.</p>
 */
public class ProveedorControllerService extends BaseService {
    
    public void findProveedores(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();
        
        List<ProveedorDTO> listaProveedorDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_NOMBRE, "");
        String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_DESCRIPCION, "");
        
        listaProveedorDTO = proveedorServiceImp.findProveedores(nombre, descripcion);
        
        if(listaProveedorDTO != null) {
            responseDTO = getResponseOk("OK", listaProveedorDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Maneja la solicitud para obtener una lista de proveedores.
     *
     * <p>Este método procesa la solicitud del cliente para obtener localidades. Dependiendo de los 
     * parámetros recibidos, puede devolver todas las localidades o solo las localidades de una 
     * provincia específica. Si se proporciona un JSON con los datos de la provincia, se filtrarán 
     * las localidades correspondientes a esa provincia. En caso contrario, se devolverán todas 
     * las localidades disponibles.</p>
     *
     * <p>La respuesta se construye y se envía al cliente en formato JSON utilizando {@link ActionController}.</p>
     *
     * @param actionController el controlador de la acción actual que maneja la solicitud y la respuesta.
     */
    public void getProveedores(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();

        List<ProveedorDTO> listaProveedorDTO = null;
        
        listaProveedorDTO = proveedorServiceImp.getProveedores();
        
        
        if (listaProveedorDTO != null) {
            responseDTO = getResponseOk("OK", listaProveedorDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Crea una nuevo proveedor en la base de datos.
     * 
     * <p>Este método toma los datos de la nuevo proveedor en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de proveedor para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createProveedor(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();
        
        String jsonProveedor = actionController.server().getRequestParameter("proveedorJSON", "");
        
        ProveedorDTO proveedorDTO = null;
        if(jsonProveedor != null && !jsonProveedor.isBlank()) {
            Gson gson = new Gson();
            proveedorDTO = gson.fromJson(jsonProveedor, ProveedorDTO.class);
            
            proveedorDTO = proveedorServiceImp.createProveedor(proveedorDTO);
        }
        
        if(proveedorDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, proveedorDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
