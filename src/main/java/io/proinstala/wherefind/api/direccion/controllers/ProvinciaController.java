
package io.proinstala.wherefind.api.direccion.controllers;

import io.proinstala.wherefind.api.direccion.services.ProvinciaControllerService;
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
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con provincias.
 *
 * Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con provincias a través de la API definida. Utiliza el servicio {@link ProvinciaControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.
 *
 *
 * La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de provincias se define como {@code /api/provincia}.
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
        PROVINCIAS,
        FIND_PROVINCIAS,
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
    
    
    /**
     * Maneja la solicitud para buscar provincias según ciertos criterios.
     *
     * <p>Este método primero verifica si el usuario está autenticado. Si el usuario
     * no está logueado, se devuelve un error 403 (Prohibido) y la ejecución se detiene.
     * Si el usuario está autenticado, la solicitud se delega al servicio {@link ProvinciaControllerService}
     * para obtener las provincias filtradas según los parámetros proporcionados.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud
     *                         y maneja la respuesta.
     */
    protected void apiFindProvincias(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.findProvincias(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener la lista de provincias.
     *
     * Este método verifica que el usuario esté autenticado y tenga los permisos necesarios. Luego, delega 
     * la solicitud al servicio {@link ProvinciaControllerService} para obtener las provincias y enviar la respuesta 
     * adecuada.
     *
     * @param actionController el {@link ActionController} que maneja la solicitud y la respuesta.
     */
    protected void apiGetProvincias(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.getProvincias(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener una provincia específica.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de provincias para obtener la provincia por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetProvincia(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.getProvinciaById(actionController);
    }
    
    /**
     * Maneja la creación de una nueva provincia utilizando los datos proporcionados en la solicitud.
     *
     * <p>Este método primero verifica si el usuario está autenticado y tiene los permisos necesarios. 
     * Si el usuario no está logueado, se envía una respuesta de error 
     * 403 (prohibido) y se interrumpe el procesamiento. Si el usuario está autenticado y autorizado, 
     * se llama al servicio de provincia para realizar la creación de la provincia.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud, 
     *                         incluyendo los datos necesarios para crear una nueva dirección.
     */
    protected void apiCreateProvincia(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        provinciaServicio.createProvincia(actionController);
    }
    
    /**
     * Maneja la solicitud para elimnar la información de una provincia específica.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de provincias para actualizar la provincia y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/provincia/delete/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiDeleteProvincia(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.deleteProvincia(actionController);
    }
    
    protected void apiUpdateProvincia(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        provinciaServicio.updateProvincia(actionController);
    }
    
    /**
     * Maneja las solicitudes HTTP GET para esta API.
     *
     * Este método procesa las solicitudes GET, obtiene el controlador de acción correspondiente y decide 
     * qué acción tomar según el tipo de acción determinado. Imprime en la salida del servidor el endpoint 
     * solicitado y delega la ejecución de la acción a métodos específicos según el tipo de acción.
     *
     * @param request el objeto {@link HttpServletRequest} que contiene la información de la solicitud.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para enviar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);
        
        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);
        
        //Tools.wait(500); //PRUEBAS PARA BORRAR
        
        switch((ActionType) actionController.actionType()) {
            case PROVINCIA -> apiGetProvincia(actionController);
            case PROVINCIAS -> apiGetProvincias(actionController);
            case FIND_PROVINCIAS -> apiFindProvincias(actionController);
              
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
        switch((ActionType) actionController.actionType()){
            case CREATE -> apiCreateProvincia(actionController);

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

        switch((ActionType) actionController.actionType()) {
            case UPDATE -> apiUpdateProvincia(actionController);
            case DELETE -> apiDeleteProvincia(actionController);
            
            default -> responseError404(actionController.server().response(), "");
        }
    }
}
