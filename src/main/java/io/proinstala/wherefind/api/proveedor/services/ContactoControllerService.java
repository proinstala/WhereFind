
package io.proinstala.wherefind.api.proveedor.services;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con contactos.
 * 
 * <p>Esta clase proporciona métodos para obtener, buscar y actualizar contactos en la base de datos.</p>
 * 
 * <p>Extiende {@link BaseService} y utiliza {@link IContactoService} para interactuar con la base de datos.</p>
 */
public class ContactoControllerService extends BaseService {
    
    public void findContactos(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();
        
        List<ContactoDTO> listaContactosDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_NOMBRE, "");
        String strIdProveedor = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_PROVEEDOR, "");
        
        int idProveedor = -1;
        try {
            idProveedor = Integer.parseInt(strIdProveedor);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaContactosDTO = contactoServiceImp.findContactos(nombre, idProveedor);
        
        if(listaContactosDTO != null) {
            responseDTO = getResponseOk("OK", listaContactosDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
