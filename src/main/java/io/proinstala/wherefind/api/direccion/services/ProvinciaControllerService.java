
package io.proinstala.wherefind.api.direccion.services;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja operaciones relacionadas con provincias.
 *
 * Esta clase se encarga de gestionar las solicitudes sobre provincias y construir 
 * las respuestas adecuadas para dichas solicitudes. Extiende {@link BaseService} y utiliza servicios 
 * específicos para interactuar con la capa de persistencia.
 */
public class ProvinciaControllerService extends BaseService {

    /**
     * Maneja la solicitud para obtener todas las provincias.
     *
     * Este método interactúa con el servicio de provincias para obtener una lista completa de todas las 
     * provincias disponibles. Luego, construye una respuesta {@link ResponseDTO} que indica el estado de 
     * la solicitud. Si la obtención de provincias es exitosa, se envía una respuesta con la lista de provincias. 
     * En caso de error, se devuelve una respuesta con un mensaje de error.
     *
     * @param actionController el {@link ActionController} que maneja la solicitud y la respuesta.
     */
    public void getProvincias(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();

        //Obtiene la lista de Provincias
        List<ProvinciaDTO> listaProvinciaDTO = provinciaServiceImp.getAllProvincias();

        if (listaProvinciaDTO != null) {
            responseDTO = getResponseOk("OK", listaProvinciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
