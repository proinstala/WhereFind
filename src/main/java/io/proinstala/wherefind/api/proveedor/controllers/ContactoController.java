
package io.proinstala.wherefind.api.proveedor.controllers;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.proveedor.services.ContactoControllerService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con contactos.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con contactos a través de la API definida. Utiliza el servicio {@link ContactoControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de contactos se define como {@code /api/contacto}.</p>
 */
@WebServlet(urlPatterns = ContactoController.BASE_API + "/*")
public class ContactoController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API = "/api/contacto";
    
    private final ContactoControllerService contactoServicio = new ContactoControllerService();

    @Override
    protected String getBaseApi() {
        return BASE_API;
    }
    
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        CONTACTO,
        FIND_CONTACTOS,
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
     * Maneja la solicitud para buscar contactos.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de contactos para buscar los contactos y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiFindContactos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        contactoServicio.findContactos(actionController);
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
            //case DIRECCION -> apiGetDireccion(actionController);
            case FIND_CONTACTOS -> apiFindContactos(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
    
}
