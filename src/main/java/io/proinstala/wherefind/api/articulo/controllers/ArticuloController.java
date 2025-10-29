
package io.proinstala.wherefind.api.articulo.controllers;

import io.proinstala.wherefind.api.articulo.services.ArticuloControllerService;
import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import static io.proinstala.wherefind.shared.controllers.BaseHttpServlet.responseError403;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador de solicitudes HTTP para manejar operaciones relacionadas con artículo.
 *
 * <p>Esta clase extiende {@link BaseHttpServlet} y se encarga de recibir y procesar las solicitudes HTTP 
 * relacionadas con artículo a través de la API definida. Utiliza el servicio {@link ArticuloControllerService} 
 * para realizar las operaciones de negocio y construir las respuestas adecuadas.</p>
 *
 * <p>La clase define una enumeración interna {@link ActionType} para representar los diferentes tipos de acción 
 * que puede manejar. La base de la URL para las API de artículo se define como {@code /api/articulo}.</p>
 */
@WebServlet(urlPatterns = ArticuloController.BASE_API + "/*")
public class ArticuloController extends BaseHttpServlet {
    
    /**
     * Base de la URL para las API de localidad.
     */
    protected static final String BASE_API = "/api/articulo";
    
    private final ArticuloControllerService articuloServicio = new ArticuloControllerService();
    
    @Override
    protected String getBaseApi() {
        return BASE_API;
    }
    
    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        ARTICULO,
        ARTICULOS,
        FIND_ARTICULOS,
        PROVEEDOR,
        PROVEEDOR_CREATE,
        PROVEEDOR_UPDATE,
        PROVEEDOR_DELETE,
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
            for (ArticuloController.ActionType accion : ArticuloController.ActionType.values()) {

                // Conprueba que action esté entre los ActionType
                if (action.equals(accion.name())) {
                    // Devuelve el ActionType encontrado
                    return accion;
                }
            }
        }

        // Devuelve el ActionType de error por no encontrar un ActionType coincidente
        return ArticuloController.ActionType.ERROR;
    }
    
    /**
     * Maneja la solicitud para obtener un artículo específico.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de artículo para obtener un artículo por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetArticulo(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.getArticuloById(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener todos los artículos.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio de artículo 
     * para obtener la lista de artículos y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetArticulos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.getArticulos(actionController);
    }
    
    /**
     * Maneja la solicitud para buscar artículos.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de artículo para buscar los artículos y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiFindArticulos(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.findArticulos(actionController);
    }
    
    /**
     * Maneja la creación de un nuevo artículo utilizando los datos proporcionados en la solicitud.
     *
     * <p>Este método primero verifica si el usuario está autenticado y tiene los permisos necesarios. 
     * Si el usuario no está logueado, se envía una respuesta de error 
     * 403 (prohibido) y se interrumpe el procesamiento. Si el usuario está autenticado y autorizado, 
     * se llama al servicio de artículo para realizar la creación del artículo.</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud, 
     *                         incluyendo los datos necesarios para crear un nuevo almacen.
     */
    protected void apiCreateArticulo(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.createArticulo(actionController);
    }
    
    /**
     * Maneja la solicitud para elimnar la información de un artículo específico.
     *
     * <p>Verifica si el usuario está autenticado y tiene los permisos necesarios. Si es así, 
     * delega la operación al servicio de artículo para borrar(activo = false) la artículo y devolver la respuesta.</p>
     * 
     * EndPoint - PUT : /api/articulo/delete/{id}
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiDeleteArticulo(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.deleteArticulo(actionController);
    }
    
    /**
     * Maneja la actualización de la información de un artículo existente.
     *
     * <p>Este método primero verifica si el usuario está autenticado y cuenta con los permisos
     * necesarios. Si el usuario no cumple con estos
     * requisitos, se envía una respuesta de error 403 (Prohibido) y se interrumpe el procesamiento.
     * En caso contrario, delega la operación al servicio de artículo para realizar la actualización
     * correspondiente y devolver la respuesta apropiada al cliente.</p>
     *
     * <p>EndPoint - PUT : /api/articulo/update/</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y la respuesta,
     *                         conteniendo la información y datos necesarios para realizar la actualización.
     */
    protected void apiUpdateArticulo(ActionController actionController) {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.updateArticulo(actionController);
    }
    
    /**
     * Maneja la solicitud para obtener un artículo proveedor específico.
     *
     * <p>Verifica si el usuario está autenticado. Si es así, delega la operación al servicio 
     * de artículo para obtener un artículo proveedor por ID y devolver la respuesta en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    protected void apiGetArticuloProveedor(ActionController actionController) {
        // Se comprueba que el usuario está logueado
        if (!UserSession.isUserLogIn(actionController.server(), false)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        
        articuloServicio.getArticuloProveedorById(actionController);
    }
    
    /**
     * Maneja la creación de una nueva asociación entre un artículo y un proveedor.
     *
     * <p>Verifica si el usuario está autenticado y tiene permisos de administrador. 
     * Si la autenticación falla, se envía una respuesta de error 403 (Prohibido). 
     * Si la autenticación es válida, se delega la operación al servicio de artículo 
     * para crear la asociación artículo-proveedor.</p>
     *
     * <p>EndPoint - POST : /api/articulo/proveedor_create</p>
     *
     * @param actionController el controlador de acción que contiene la información de la solicitud, 
     *                         incluyendo los datos necesarios para crear la asociación.
     */
    protected void apiProveedorCreate(ActionController actionController) {
        if (!UserSession.isUserLogIn(actionController.server(), true)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        articuloServicio.createArticuloProveedor(actionController);
    }

    /**
     * Maneja la actualización de los datos de la asociación entre un artículo y un proveedor.
     *
     * <p>Este método verifica si el usuario está autenticado y cuenta con los permisos necesarios. 
     * Si no es así, se envía una respuesta de error 403 (Prohibido). 
     * Si el usuario está autorizado, se delega la actualización al servicio de artículo.</p>
     *
     * <p>EndPoint - PUT : /api/articulo/proveedor_update</p>
     *
     * @param actionController el controlador de acción que maneja la solicitud y respuesta.
     */
    protected void apiProveedorUpdate(ActionController actionController) {
        if (!UserSession.isUserLogIn(actionController.server(), true)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        articuloServicio.updateArticuloProveedor(actionController);
    }

    /**
     * Maneja la eliminación (desactivación lógica) de la asociación entre un artículo y un proveedor.
     *
     * <p>Verifica si el usuario está autenticado y autorizado. Si no cumple con los requisitos,
     * se envía una respuesta 403 (Prohibido). Si el usuario tiene permisos válidos, 
     * se delega la operación al servicio de artículo para eliminar la asociación.</p>
     *
     * <p>EndPoint - PUT : /api/articulo/proveedor_delete/{id}</p>
     *
     * @param actionController el controlador de acción que maneja la solicitud y respuesta.
     */
    protected void apiProveedorDelete(ActionController actionController) {
        if (!UserSession.isUserLogIn(actionController.server(), true)) {
            responseError403(actionController.server().response(), "");
            return;
        }
        articuloServicio.deleteArticuloProveedor(actionController);
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
        
        switch((ArticuloController.ActionType) actionController.actionType()) {
            case ARTICULO -> apiGetArticulo(actionController);
            case ARTICULOS -> apiGetArticulos(actionController);
            case FIND_ARTICULOS -> apiFindArticulos(actionController);
            case PROVEEDOR -> apiGetArticuloProveedor(actionController);
              
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
        switch((ArticuloController.ActionType) actionController.actionType()){
            case CREATE -> apiCreateArticulo(actionController);
            case PROVEEDOR_CREATE -> apiProveedorCreate(actionController);

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

        switch((ArticuloController.ActionType) actionController.actionType()) {
            case UPDATE -> apiUpdateArticulo(actionController);
            case DELETE -> apiDeleteArticulo(actionController);
            case PROVEEDOR_UPDATE -> apiProveedorUpdate(actionController);
            case PROVEEDOR_DELETE -> apiProveedorDelete(actionController);
            
            default -> responseError404(actionController.server().response(), "");
        }
    }

    
}
