
package io.proinstala.wherefind.api.direccion.controllers;

import io.proinstala.wherefind.api.direccion.services.DireccionControllerService;
import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import static io.proinstala.wherefind.shared.controllers.BaseHttpServlet.responseError403;
import static io.proinstala.wherefind.shared.controllers.BaseHttpServlet.responseError404;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con direcciones.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con direcciones a través de la API definida. Utiliza el servicio {@link DireccionControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de direcciones se define como {@code /api/direccion}.</p>
 */
@WebServlet(urlPatterns = DireccionController.BASE_API_DIRECCION + "/*")
public class DireccionController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API_DIRECCION = "/api/direccion";
    
    //Servicio encargado de manejar la lógica de negocio para las direcciones
    private final DireccionControllerService direccionServicio = new DireccionControllerService();
       
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        DIRECCION,
        FINDDIRECCIONES,
        UPDATE,
        CREATE,
        DELETE
    }
    
    /**
     * Obtiene la base de la URL de la API direccion.
     *
     * @return la base de la URL de la API.
     */
    @Override
    protected String getBaseApi() {
        return BASE_API_DIRECCION;
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
     * Maneja la solicitud para buscar direcciones.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de direcciones para buscar las direcciones y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiFindDirecciones(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        direccionServicio.findDirecciones(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener una dirección específica.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de direcciones para obtener la dirección por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetDireccion(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        direccionServicio.getDireccionById(actionController);
    }
      
    /**
     * Maneja la solicitud para actualizar la información de una dirección específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de direcciones para actualizar la dirección y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/direccion/update/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiUpdateDireccion(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        direccionServicio.updateDireccion(actionController);
    }
    
    /**
     * Maneja la solicitud para elimnar la información de una dirección específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de direcciones para actualizar la dirección y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/direccion/delete/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */

    protected void apiDeleteDireccion(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        direccionServicio.deleteDireccion(actionController);
    }
    
    /**
     * Maneja la creación de una nueva dirección utilizando los datos proporcionados en la solicitud.
     *
     * <p>Este método primero verifica si el usuario está autenticado. 
     * Si el usuario no está logueado, se envía una respuesta de error 
     * 403 (prohibido) y se interrumpe el procesamiento. Si el usuario está autenticado y autorizado, 
     * se llama al servicio de direcciones para realizar la creación de la dirección.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud, 
     *                         incluyendo los datos necesarios para crear una nueva dirección.
     */
    protected void apiCreateDireccion(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        direccionServicio.createDireccion(actionController); 
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
            case DIRECCION -> apiGetDireccion(actionController);
            case FINDDIRECCIONES -> apiFindDirecciones(actionController);
              
            default -> responseError403(actionController.server().response(), "");
        }
    }
    
    /**
     * Maneja las solicitudes HTTP PUT para las acciones definidas.
     *
     * <p>Obtiene la acción solicitada y determina el tipo de acción. Según el tipo, realiza 
     * la operación correspondiente llamando a los métodos adecuados o devuelve un error si 
     * la acción no es válida.</p>
     *
     * @param request  la solicitud HTTP recibida.
     * @param response la respuesta HTTP que se enviará.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint PUT : " + actionController.parametros()[0]);

        switch((ActionType) actionController.actionType()) {
            case UPDATE -> apiUpdateDireccion(actionController);
            case DELETE -> apiDeleteDireccion(actionController);
            
            default -> responseError404(actionController.server().response(), "");
        }
    }
    
    /**
     * Maneja las solicitudes HTTP POST para las acciones definidas.
     *
     * <p>Este método se encarga de procesar las solicitudes POST que llegan al servlet. Primero, 
     * obtiene un objeto {@link ActionController} a partir del {@link HttpServletRequest} y 
     * {@link HttpServletResponse}. Luego, imprime en el log el punto de entrada (endPoint) 
     * de la solicitud para fines de depuración. Dependiendo del tipo de acción ({@link ActionType}) 
     * especificado en la solicitud, ejecuta la acción correspondiente. Si el tipo de acción no 
     * coincide con ninguno de los casos definidos, se devuelve un error 404 (no encontrado).</p>
     *
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para enviar una respuesta al cliente.
     * @throws ServletException si ocurre un error en el procesamiento del servlet.
     * @throws IOException si ocurre un error de entrada/salida durante el procesamiento de la solicitud o respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint POST : " + actionController.parametros()[0]);

        // Dependiendo del ActionType, realizará una acción
        switch((ActionType) actionController.actionType()){
            case CREATE -> apiCreateDireccion(actionController);

            default -> responseError404(actionController.server().response(), "");
        }
    }
    
}
