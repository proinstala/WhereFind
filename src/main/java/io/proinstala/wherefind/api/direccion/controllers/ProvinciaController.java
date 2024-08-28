
package io.proinstala.wherefind.api.direccion.controllers;

import io.proinstala.wherefind.api.direccion.services.ProvinciaControllerService;
import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import static io.proinstala.wherefind.shared.controllers.BaseHttpServlet.responseError403;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Controlador de API direccion que maneja las solicitudes HTTP relacionadas con la gestión de provincias.
 */
@WebServlet(urlPatterns = ProvinciaController.BASE_API_PROVINCIA + "/*")
public class ProvinciaController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de provincia.
     */
    protected static final String BASE_API_PROVINCIA = "/api/provincia";
    
    private final ProvinciaControllerService provinciaServicio = new ProvinciaControllerService();

    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        PROVINCIA,
        PROVINCIAS
    }
    
    /**
     * Obtiene la base de la URL de la API direccion.
     *
     * @return la base de la URL de la API.
     */
    @Override
    protected String getBaseApi() {
        return BASE_API_PROVINCIA;
    }
    
    /**
     * Determina el tipo de acción basado en el nombre de la acción.
     *
     * @param action el nombre de la acción.
     * @return el tipo de acción correspondiente, o {@code ActionType.ERROR} si no se encuentra.
     */
    @Override
    protected Object getActionType(String action)
    {
        // Si la acción no es nula
        if (action != null && !action.isBlank()) {
            // Convierte el texto en mayúsculas
            action = action.toUpperCase();

            // Recorre todos los ActionType
            for (ActionType accion : ActionType.values()) {

                // Conprueba que action esté entre los ActionType
                if (action.equals(accion.name())) {
                    // Devuelve el ActionType encontrado
                    return accion;
                }
            }
        }

        // Devuelve el ActionType de error por no encontrar un ActionType coincidente
        return ActionType.ERROR;
    }
    
    protected void apiGetProvincias(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.getProvincias(actionController);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);
        
        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);
        
        switch((ActionType) actionController.actionType()) {
            case PROVINCIA -> System.out.println("SE PIDE PROVINCIA");
            case PROVINCIAS -> apiGetProvincias(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
}
