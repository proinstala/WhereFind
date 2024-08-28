
package io.proinstala.wherefind.api.direccion.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class LocalidadControllerService extends BaseService {
    
    public void getLocalidades(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestionPersistencia.getLocalidadService();
        
        List<LocalidadDTO> listaLocalidadesDTO = null;
        
        
        String jsonProvincia = actionController.server().getRequestParameter("jsonProvincia", "");
        
        if(jsonProvincia != null && !jsonProvincia.isBlank()) {
            Gson gson = new Gson();
            ProvinciaDTO provinciaDTO = gson.fromJson(jsonProvincia, ProvinciaDTO.class);
            
            //Obtiene la lista de Provincias
            listaLocalidadesDTO = localidadServiceImp.getLocalidadesOfProvincia(provinciaDTO);
        } else {
            //Obtiene la lista de Provincias
            listaLocalidadesDTO = localidadServiceImp.getAllLocalidades();
        }
        
        
        if (listaLocalidadesDTO != null) {
            responseDTO = getResponseOk("OK", listaLocalidadesDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
}
