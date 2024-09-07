
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
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con localidades.
 *
 * Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con localidades a través de la API definida. Utiliza el servicio {@link LocalidadControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.
 *
 *
 * La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de localidades se define como {@code /api/localidad}.
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
     * Obtiene la base de la URL de la API localidad.
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
    
    /**
     * Maneja la solicitud para obtener todas las localidades.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio de localidades 
     * para obtener la lista de localidades y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetLocalidades(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        localidadServicio.getLocalidades(actionController);
    }
    
    /**
     * Maneja las solicitudes HTTP GET para las acciones definidas.
     *
     * <p>Obtiene la acción solicitada y determina el tipo de acción. Según el tipo, realiza 
     * la operación correspondiente llamando a los métodos adecuados o devuelve un error si 
     * la acción no es válida.</p>
     *
     * @param request  la solicitud HTTP recibida.
     * @param response la respuesta HTTP que se enviará.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);
        
        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);
        
        switch((ActionType) actionController.actionType()) {
            case LOCALIDAD -> System.out.println("SE PIDE PROVINCIA");
            case LOCALIDADES -> apiGetLocalidades(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
    
}
