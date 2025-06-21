
package io.proinstala.wherefind.api.proveedor.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProveedorService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
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
     * Obtiene un proveedor por su identificador.
     * 
     * <p>Este método extrae el identificador del proveedor del controlador de acción, utiliza el
     * servicio de proveedor para recuperar los datos del proeedor correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getProveedorById(ActionController actionController) {
        ResponseDTO responseDTO;
        ProveedorDTO proveedorDTO;
        
        // Conecta con el Gestor de Persistencia
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();
        
        int idProveedor = -1;
        try {
            String id = actionController.server().getRequestParameter("idProveedor", "-1");
            idProveedor = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        proveedorDTO = proveedorServiceImp.getProveedorById(idProveedor);
        
        if (proveedorDTO != null) {
            responseDTO = getResponseOk("OK", proveedorDTO, 0);
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
    
    /**
     * Actualiza la información de un proveedor existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar un proveedor en la base de
     * datos. Utiliza el servicio de proveedor para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateProveedor(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id del proveedor desde el parámetro 1 de la lista de parámetros
        int id = actionController.getIntFromParametros(1);

        // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
        // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
        if (id == -1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        // Crea la respuesta con un error
        responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);

        // Conecta con el Gestor de Persistencia
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();

        ProveedorDTO proveedorDTO = proveedorServiceImp.getProveedorById(id);

        if(proveedorDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_NOMBRE, "");
            String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_DESCRIPCION, "");
            String paginaWeb = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_PAGINA_WEB, "");
            String strDirecion = actionController.server().getRequestParameter(FormParametros.PARAM_PROVEEDOR_DIRECCION, "");

            try {
                int direccionId = Integer.parseInt(strDirecion);
                proveedorDTO.setNombre(nombre);
                proveedorDTO.setDescripcion(descripcion);
                proveedorDTO.setPaginaWeb(paginaWeb);
                
                proveedorDTO.setDireccion(DireccionDTO.builder().id(direccionId).build());
                

                if (proveedorServiceImp.updateProveedor(proveedorDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, proveedorDTO, 0);
                } else {
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }

            } catch (NumberFormatException e) {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            } catch (Exception e) {
                responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        }
        responseJson(actionController.server().response(), responseDTO);
    } 
    
    /**
     * Elimina un proveedor por su identificador.
     * 
     * <p>Este método extrae el identificador del proveedor del controlador de acción, utiliza el
     * servicio de proveedor para eliminar el proveedor correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteProveedor(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id de la localidad desde el parámetro 1 de la lista de parámetros
        int id = actionController.getIntFromParametros(1);

        // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
        // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
        if (id == -1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        
        // Conecta con el Gestor de Persistencia
        IProveedorService proveedorServiceImp = GestorPersistencia.getProveedorService();
        
        if(proveedorServiceImp.deleteProveedor(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
}
