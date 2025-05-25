
package io.proinstala.wherefind.api.proveedor.controllers;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.proveedor.services.PuestoTrabajoControllerService;

import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con puesto trabajo.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con puesto trabajo a través de la API definida. Utiliza el servicio {@link PuestoTrabajoControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de puesto trabajo se define como {@code /api/puesto}.</p>
 */
@WebServlet(urlPatterns = PuestoTrabajoController.BASE_API + "/*")
public class PuestoTrabajoController extends BaseHttpServlet {
    
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API = "/api/puesto";
    
    private final PuestoTrabajoControllerService puestoTrabajoServicio = new PuestoTrabajoControllerService();

    @Override
    protected String getBaseApi() {
        return BASE_API;
    }
    
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        PUESTO,
        PUESTOS,
        FIND_PUESTO,
        UPDATE,
        CREATE,
        DELETE
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
     * Maneja la solicitud para obtener un puesto específico.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de puestos de trabajo para obtener el puesto por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetPuesto(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        puestoTrabajoServicio.getPuestoById(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener todas los puestos.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio de puestos de trabajo 
     * para obtener la lista de puestos y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetPuestos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        puestoTrabajoServicio.getPuestostrabajo(actionController);
    }
    
    /**
     * Maneja la solicitud para buscar puestos según ciertos criterios.
     *
     * <p>Este método primero verifica si el usuario está autenticado. Si el usuario
     * no está logueado, se devuelve un error 403 (Prohibido) y la ejecución se detiene.
     * Si el usuario está autenticado, la solicitud se delega al servicio {@link PuestoTrabajoControllerService}
     * para obtener los puestos filtrados según los parámetros proporcionados.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud
     *                         y maneja la respuesta.
     */
    protected void apiFindPuesto(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        puestoTrabajoServicio.findPuestoTrabajo(actionController);
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
            case PUESTO -> apiGetPuesto(actionController);
            case PUESTOS -> apiGetPuestos(actionController);
            case FIND_PUESTO -> apiFindPuesto(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
}
