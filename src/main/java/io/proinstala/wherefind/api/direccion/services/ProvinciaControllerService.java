
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


public class ProvinciaControllerService extends BaseService {

    public void getProvincias(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // onecta con el Gestor de Permanencia
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
