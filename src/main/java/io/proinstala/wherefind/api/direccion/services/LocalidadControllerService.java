
package io.proinstala.wherefind.api.direccion.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
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
 * Servicio que maneja operaciones relacionadas con localidades.
 *
 * Esta clase se encarga de gestionar las solicitudes sobre localidades y construir 
 * las respuestas adecuadas para dichas solicitudes. Extiende {@link BaseService} y utiliza servicios 
 * específicos para interactuar con la capa de persistencia.
 */
public class LocalidadControllerService extends BaseService {

    /**
     * Maneja la solicitud para obtener una lista de localidades.
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
    public void getLocalidades(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();

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
