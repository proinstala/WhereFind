
package io.proinstala.wherefind.api.direccion.controllers;

import io.proinstala.wherefind.api.direccion.services.LocalidadControllerService;
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
@WebServlet(urlPatterns = LocalidadController.BASE_API_LOCALIDAD + "/*")
public class LocalidadController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API_LOCALIDAD = "/api/localidad";
    
    private final LocalidadControllerService localidadServicio = new LocalidadControllerService();
    
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        LOCALIDAD,
        LOCALIDADES
    }
    
    
    /**
     * Obtiene la base de la URL de la API direccion.
     *
     * @return la base de la URL de la API.
     */
    @Override
    protected String getBaseApi() {
        return BASE_API_LOCALIDAD;
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
        if (action != "") {
            // Convierte el texto en mayúsculas
            action = action.toUpperCase();

            // Recorre todos los ActionType
            for (LocalidadController.ActionType accion : LocalidadController.ActionType.values()) {

                // Conprueba que action esté entre los ActionType
                if (action.equals(accion.name())) {
                    // Devuelve el ActionType encontrado
                    return accion;
                }
            }
        }

        // Devuelve el ActionType de error por no encontrar un ActionType coincidente
        return ProvinciaController.ActionType.ERROR;
    }
    
    protected void apiGetLocalidades(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        localidadServicio.getLocalidades(actionController);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);
        
        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);
        
        switch(actionController.actionType()) {
            case LocalidadController.ActionType.LOCALIDAD -> System.out.println("SE PIDE PROVINCIA");
            case LocalidadController.ActionType.LOCALIDADES -> apiGetLocalidades(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
    
}
