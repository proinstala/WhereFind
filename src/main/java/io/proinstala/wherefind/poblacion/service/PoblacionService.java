package io.proinstala.wherefind.poblacion.service;

import java.util.ArrayList;
import java.util.List;

import io.proinstala.wherefind.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.infraestructure.data.interfaces.IPoblacionService;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.PoblacionDTO;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.textos.LocaleApp;

public class PoblacionService extends BaseService {

    record ResponseDTO(int isError, int isUrl, String result, Object user) {}

    protected ResponseDTO createResponseDTO(int isError, int isUrl, String mensaje, Object objecto)
    {
        return new ResponseDTO(isError, isUrl, mensaje, objecto);
    }

    protected ResponseDTO getResponseError(String mensaje, Object objecto)
    {
        return createResponseDTO(1, 0, mensaje, objecto);
    }

    protected ResponseDTO getResponseOk(String mensaje, Object objecto, int isUrl)
    {
        return createResponseDTO(0, isUrl, mensaje, objecto);
    }


    public void getPoblaciones(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Conecta con el Gestor de Permanencia
        IPoblacionService gestor = GestionPersistencia.getPoblacionService();

        // Obtiene la lista de usuarios
        List<PoblacionDTO> listado = gestor.getAllPoblaciones();

        // Si la lista devuelta no es nula
        if (listado != null)
        {
            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
            response = getResponseOk("OK", listado, 0);
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }

}
