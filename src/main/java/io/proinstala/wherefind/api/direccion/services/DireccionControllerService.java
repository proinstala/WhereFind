
package io.proinstala.wherefind.api.direccion.services;

import io.proinstala.wherefind.api.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class DireccionControllerService extends BaseService{
    
    public void findDirecciones(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IDireccionService direccionServiceImp = GestionPersistencia.getDireccionService();
        
        List<DireccionDTO> listaDireccionesDTO = null;
        
        String calle = actionController.server().getRequestParameter("calle", "");
        String strIdLocalidad = actionController.server().getRequestParameter("localidad", "");
        String strIdProvincia = actionController.server().getRequestParameter("provincia", "");
        
        int idLocalidad = -1;
        try {
            idLocalidad = Integer.parseInt(strIdLocalidad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        int idProvincia = -1;
        try {
            idProvincia = Integer.parseInt(strIdProvincia);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaDireccionesDTO = direccionServiceImp.findDirecciones(calle, idLocalidad, idProvincia);
        
        if(listaDireccionesDTO != null) {
            responseDTO = getResponseOk("OK", listaDireccionesDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
}
