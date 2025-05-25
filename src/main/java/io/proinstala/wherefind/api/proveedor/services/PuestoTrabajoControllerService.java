
package io.proinstala.wherefind.api.proveedor.services;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IPuestoTrabajoService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class PuestoTrabajoControllerService extends BaseService {
    
    /**
     * Busca puesto según el nombre proporcionado y devuelve la respuesta en formato JSON.
     *
     * <p>Este método obtiene el parámetro de búsqueda desde la solicitud HTTP y lo utiliza para 
     * recuperar una lista de puestos desde el servicio de persistencia. Si la búsqueda tiene 
     * éxito, devuelve una respuesta con la lista de puestos encontradas. En caso contrario, 
     * se envía una respuesta de error.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    public void findPuestoTrabajo(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IPuestoTrabajoService puestoTrabajoServiceImp = GestorPersistencia.getPuestoTrabajoService();
        
        List<PuestoTrabajoDTO> listaPuestoTrabajoDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_PUESTO_TRABAJO_NOMBRE, "");
        
        listaPuestoTrabajoDTO = puestoTrabajoServiceImp.findPuestos(nombre);
        
        if(listaPuestoTrabajoDTO != null) {
            responseDTO = getResponseOk("OK", listaPuestoTrabajoDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Maneja la solicitud para obtener todos los puesto de trabajo.
     *
     * Este método interactúa con el servicio de puesto de trabajo para obtener una lista completa de todos los 
     * puestos disponibles. Luego, construye una respuesta {@link ResponseDTO} que indica el estado de 
     * la solicitud. Si la obtención de puestos es exitosa, se envía una respuesta con la lista de puestos. 
     * En caso de error, se devuelve una respuesta con un mensaje de error.
     *
     * @param actionController el {@link ActionController} que maneja la solicitud y la respuesta.
     */
    public void getPuestostrabajo(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IPuestoTrabajoService puestoTrabajoServiceImp = GestorPersistencia.getPuestoTrabajoService();

        List<PuestoTrabajoDTO> listaPuestoTrabajoDTO = null;
        
        listaPuestoTrabajoDTO = puestoTrabajoServiceImp.getAllPuestos();
        
        
        if (listaPuestoTrabajoDTO != null) {
            responseDTO = getResponseOk("OK", listaPuestoTrabajoDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Obtiene un puesto de trabajo por su identificador.
     * 
     * <p>Este método extrae el identificador del puesto de trabajo del controlador de acción, utiliza el
     * servicio de puesto trabajo para recuperar los datos del puesto correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getPuestoById(ActionController actionController) {
        ResponseDTO responseDTO;
        PuestoTrabajoDTO puestoTrabajoDTO;
        
        // Conecta con el Gestor de Persistencia
        IPuestoTrabajoService puestoTrabajoServiceImp = GestorPersistencia.getPuestoTrabajoService();
        
        int idPuesto = -1;
        try {
            String id = actionController.server().getRequestParameter("idPuesto", "-1");
            idPuesto = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        puestoTrabajoDTO = puestoTrabajoServiceImp.getPuestoById(idPuesto);
        
        if (puestoTrabajoDTO != null) {
            responseDTO = getResponseOk("OK", puestoTrabajoDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
