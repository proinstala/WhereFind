
package io.proinstala.wherefind.api.almacen.controllers;

import io.proinstala.wherefind.api.almacen.services.ExistenciaControllerService;
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
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con existencia.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas las existencias a través de la API definida. Utiliza el servicio {@link ExistenciaControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de existencia se define como {@code /api/existencia}.</p>
 */
@WebServlet(urlPatterns = ExistenciaController.BASE_API + "/*")
public class ExistenciaController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API = "/api/existencia";
    
    private final ExistenciaControllerService existenciaServicio = new ExistenciaControllerService();
    
    @Override
    protected String getBaseApi() {
        return BASE_API;
    }
    
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        EXISTENCIA,
        EXISTENCIAS,
        FIND_EXISTENCIAS,
        FIND_EXISTENCIAS_ARTICULO,
        UPDATE,
        CREATE,
        DELETE,
        DISABLE,
        ENABLE
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
            for (ExistenciaController.ActionType accion : ExistenciaController.ActionType.values()) {

                // Conprueba que action esté entre los ActionType
                if (action.equals(accion.name())) {
                    // Devuelve el ActionType encontrado
                    return accion;
                }
            }
        }

        // Devuelve el ActionType de error por no encontrar un ActionType coincidente
        return ExistenciaController.ActionType.ERROR;
    }
    
    /**
     * Maneja la solicitud para obtener una existencia específica.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de existencia para obtener una existencia por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetExistencia(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.getExistenciaById(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener todos las existencias.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio de existencia
     * para obtener la lista de existencias y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetExistencias(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        //existenciaServicio.getExistencias(actionController);
    }
    
    /**
     * Maneja la solicitud para buscar existencias.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de existencia para buscar las existencias y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiFindExistencias(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.findExistencias(actionController);
    }
    
    
    /**
     * Maneja la solicitud para buscar existencias.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de existencia para buscar las existencias y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiFindExistenciasByArticulo(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.findExistenciasByArticulo(actionController);
    }
    
    /**
     * Maneja la creación de una nueva existencia utilizando los datos proporcionados en la solicitud.
     *
     * <p>Este método primero verifica si el usuario está autenticado y tiene los permisos necesarios. 
     * Si el usuario no está logueado, se envía una respuesta de error 
     * 403 (prohibido) y se interrumpe el procesamiento. Si el usuario está autenticado y autorizado, 
     * se llama al servicio de existencia para realizar la creación de la existencia.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud, 
     *                         incluyendo los datos necesarios para crear un nuevo almacen.
     */
    protected void apiCreateExistencia(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.createExistencia(actionController);
    }
    
    /**
     * Maneja la solicitud para deshabilitar una existencia específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de existencia para deshabilitar la existencia y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/existencia/disable/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiDisableExistencia(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        //existenciaServicio.disableExistencia(actionController);
    }
    
    /**
     * Maneja la solicitud para habilitar una existencia específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de existencia para deshabilitar la existencia y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/existencia/enable/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiDisableEnable(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        //existenciaServicio.enableExistencia(actionController);
    }
    
    /**
     * Maneja la solicitud para elimnar la información de una existencia específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de existencia para borrar la existencia y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/existencia/delete/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiDeleteExistencia(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.deleteExistencia(actionController);
    }
    
    /**
     * Maneja la actualización de la información de una existencia existente.
     *
     * <p>Este método primero verifica si el usuario está autenticado y cuenta con los permisos
     * necesarios. Si el usuario no cumple con estos
     * requisitos, se envía una respuesta de error 403 (Prohibido) y se interrumpe el procesamiento.
     * En caso contrario, delega la operación al servicio de existencia para realizar la actualización
     * correspondiente y devolver la respuesta apropiada al cliente.</p>
     *
     * <p>EndPoint - PUT : /api/existencia/update/</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y la respuesta,
     *                         conteniendo la información y datos necesarios para realizar la actualización.
     */
    protected void apiUpdateExistencia(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        existenciaServicio.updateExistencia(actionController);
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
        
        switch((ExistenciaController.ActionType) actionController.actionType()) {
            case EXISTENCIA -> apiGetExistencia(actionController);
            case EXISTENCIAS -> apiGetExistencias(actionController);
            case FIND_EXISTENCIAS -> apiFindExistencias(actionController);
            case FIND_EXISTENCIAS_ARTICULO -> apiFindExistenciasByArticulo(actionController);
              
            default -> responseError403(actionController.server().response(), "");
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
        switch((ExistenciaController.ActionType) actionController.actionType()){
            case CREATE -> apiCreateExistencia(actionController);

            default -> responseError404(actionController.server().response(), "");
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

        switch((ExistenciaController.ActionType) actionController.actionType()) {
            case UPDATE -> apiUpdateExistencia(actionController);
            case DELETE -> apiDeleteExistencia(actionController);
            //CASE DISABLE -> apiDisableExistencia(actionController);
            
            default -> responseError404(actionController.server().response(), "");
        }
    }
    
    
}
