
package io.proinstala.wherefind.api.almacen.services;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ITipoEmplazamientoService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.dtos.TipoEmplazamientoDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;


/**
 * Servicio controlador para gestionar tipos de emplazamiento.
 * 
 * @author David
 */
public class TipoEmplazamientoControllerService extends BaseService {
    
    /**
     * Busca tipos de emplazamiento según el nombre proporcionado y devuelve la respuesta en formato JSON.
     *
     * <p>Obtiene el parámetro de búsqueda desde la solicitud HTTP y lo utiliza para recuperar una lista de tipos de emplazamiento
     * desde el servicio de persistencia. Si la búsqueda tiene éxito, devuelve una respuesta con la lista encontrada. En caso contrario,
     * se envía una respuesta de error.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    public void findTipoEmplazamiento(ActionController actionController) {
        ResponseDTO responseDTO;
        
        ITipoEmplazamientoService tipoEmplazamientoService = GestorPersistencia.getTipoEmplazamientoService();
        
        List<TipoEmplazamientoDTO> listaTipos = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_TIPO_EMPLAZAMIENTO_NOMBRE, "");
        
        listaTipos = tipoEmplazamientoService.findTiposEmplazamiento(nombre);
        
        if(listaTipos != null) {
            responseDTO = getResponseOk("OK", listaTipos, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Maneja la solicitud para obtener todos los tipos de emplazamiento.
     *
     * <p>Obtiene una lista completa de todos los tipos de emplazamiento disponibles y devuelve la respuesta en formato JSON.
     * En caso de error, se devuelve una respuesta con un mensaje adecuado.</p>
     *
     * @param actionController el controlador que maneja la solicitud y respuesta.
     */
    public void getAllTiposEmplazamiento(ActionController actionController) {
        ResponseDTO responseDTO;

        ITipoEmplazamientoService tipoEmplazamientoService = GestorPersistencia.getTipoEmplazamientoService();

        List<TipoEmplazamientoDTO> listaTipos = null;
        
        listaTipos = tipoEmplazamientoService.getAllTiposEmplazamiento();
        
        if (listaTipos != null) {
            responseDTO = getResponseOk("OK", listaTipos, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene un tipo de emplazamiento por su identificador.
     * 
     * <p>Extrae el identificador del tipo de emplazamiento del controlador de acción, utiliza el
     * servicio para recuperar los datos correspondientes, y devuelve la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador que contiene los parámetros de la solicitud.
     */
    public void getTipoEmplazamientoById(ActionController actionController) {
        ResponseDTO responseDTO;
        TipoEmplazamientoDTO tipoEmplazamientoDTO;
        
        ITipoEmplazamientoService tipoEmplazamientoService = GestorPersistencia.getTipoEmplazamientoService();
        
        int idTipo = -1;
        try {
            String id = actionController.server().getRequestParameter("idTipoEmplazamiento", "-1");
            idTipo = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        tipoEmplazamientoDTO = tipoEmplazamientoService.getTipoEmplazamientoById(idTipo);
        
        if (tipoEmplazamientoDTO != null) {
            responseDTO = getResponseOk("OK", tipoEmplazamientoDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    
}
