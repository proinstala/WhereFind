
package io.proinstala.wherefind.api.almacen.controllers;

import io.proinstala.wherefind.api.almacen.services.TipoEmplazamientoControllerService;
import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import static io.proinstala.wherefind.shared.controllers.BaseHttpServlet.responseError403;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con tipo emplazamiento.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con tipo emplazamiento a través de la API definida. Utiliza el servicio {@link TipoEmplazamientoControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de tipo emplazamiento se define como {@code /api/tipo_emplazamiento}.</p>
 */
@WebServlet(urlPatterns = TipoEmplazamientoController.BASE_API + "/*")
public class TipoEmplazamientoController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API = "/api/tipo_emplazamiento";
    
    private final TipoEmplazamientoControllerService tipoEmplazamientoService = new TipoEmplazamientoControllerService();
    
    @Override
    protected String getBaseApi() {
        return BASE_API;
    }

    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        TIPO_EMPLAZAMIENTO,
        TIPOS_EMPLAZAMIENTOS,
        FIND_TIPOS_EMPLAZAMIENTOS,
    }
    
    /**
     * Determina el tipo de acción basado en el nombre de la acción.
     *
     * @param action el nombre de la acción.
     * @return el tipo de acción correspondiente, o {@code ActionType.ERROR} si no se encuentra.
     */
    @Override
    protected Object getActionType(String action) {
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
     * Maneja la solicitud para obtener un tipo emplazamiento específico.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de tipo emplazamiento para obtener el tipo emplazamiento por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetTipoEmplazamiento(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        tipoEmplazamientoService.getTipoEmplazamientoById(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener todos los tipos emplazamientos.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio de tipo emplazamiento
     * para obtener la lista de tipos emplazamiento y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetTiposEmplazamientos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }

        tipoEmplazamientoService.getAllTiposEmplazamiento(actionController);
    }
    
    /**
     * Maneja la solicitud para buscar tipos emplazamiento según ciertos criterios.
     *
     * <p>Este método primero verifica si el usuario está autenticado. Si el usuario
     * no está logueado, se devuelve un error 403 (Prohibido) y la ejecución se detiene.
     * Si el usuario está autenticado, la solicitud se delega al servicio {@link TipoEmplazamientoControllerService}
     * para obtener los tipos de emplazamientos filtrados según los parámetros proporcionados.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud
     *                         y maneja la respuesta.
     */
    protected void apiFindTiposEmplazamientos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        tipoEmplazamientoService.findTiposEmplazamientos(actionController);
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
        
        switch((TipoEmplazamientoController.ActionType) actionController.actionType()) {
            case TIPO_EMPLAZAMIENTO -> apiGetTipoEmplazamiento(actionController);
            case TIPOS_EMPLAZAMIENTOS -> apiGetTiposEmplazamientos(actionController);
            case FIND_TIPOS_EMPLAZAMIENTOS -> apiFindTiposEmplazamientos(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
    
}
