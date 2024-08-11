package io.proinstala.wherefind.api.identidad.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.identidad.services.IdentidadService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Controlador de la identidad que maneja las solicitudes HTTP relacionadas con la gestión de usuarios.
 */
@WebServlet(urlPatterns = IdentidadController.BASE_API_IDENTIDAD + "/*")
public class IdentidadController  extends BaseHttpServlet {

    /**
     * Base de la URL para las API de identidad.
     */
    protected static final String BASE_API_IDENTIDAD = "/api/identidad";

    // Se declara el servicio que realmente gestiona todas las acciones relacionadas con la api
    private final IdentidadService identidadServicio = new IdentidadService();

    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        LOGOUT,
        LOGIN,
        USER,
        USERS,
        DELETE,
        UPDATE,
        UPDATEPASSWORD,
        CREATE,
        RECOVERY
    }

    /**
     * Obtiene la base de la URL de la API.
     *
     * @return la base de la URL de la API.
     */
    @Override
    protected String getBaseApi()
    {
        return BASE_API_IDENTIDAD;
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
     * Maneja la solicitud de cierre de sesión.
     *
     * EndPoint - GET : /api/identidad/logout
     *
     * @param actionController el controlador de acción.
     */
    protected void apiLogOut(ActionController actionController)
    {
        // Se llama al servicio para procese la acción requerida
        identidadServicio.logOut(actionController.server());
    }

    /**
     * Maneja la solicitud de inicio de sesión.
     *
     * EndPoint - POST : /api/identidad/login
     *
     * @param actionController el controlador de acción.
     */
    protected void apiLogIn(ActionController actionController)
    {
        // Se llama al servicio para procese la acción requerida
        identidadServicio.logIn(actionController.server());
    }

    /**
     * Maneja la solicitud para obtener la información de un usuario específico.
     *
     * EndPoint - GET : /api/identidad/user/{id}
     *
     * @param actionController el controlador de acción.
     */
    protected void apiGetUser(ActionController actionController)
    {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        // Se llama al servicio para procese la acción requerida
        identidadServicio.getUser(actionController);
    }

    /**
     * Maneja la solicitud para obtener la lista de usuarios.
     *
     * EndPoint - GET : /api/identidad/users
     *
     * @param actionController el controlador de acción.
     */
    protected void apiGetUsers(ActionController actionController)
    {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }


        // Se llama al servicio para procese la acción requerida
        identidadServicio.getUsers(actionController);
    }

    /**
     * Maneja la solicitud para eliminar un usuario específico.
     *
     * EndPoint - PUT : /api/identidad/delete/{id}
     *
     * @param actionController el controlador de acción.
     */
    protected void apiDeleteUser(ActionController actionController)
    {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        // Se llama al servicio para procese la acción requerida
        identidadServicio.deleteUser(actionController);
    }

    /**
     * Maneja la solicitud para actualizar la información de un usuario específico.
     *
     * EndPoint - PUT : /api/identidad/update/{id}
     *
     * @param actionController el controlador de acción.
     */
    protected void apiUpdateUser(ActionController actionController)
    {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), false))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        // Se llama al servicio para procese la acción requerida
        identidadServicio.updateUser(actionController);
    }


    /**
     * Maneja la solicitud para actualizar el password de un usuario específico.
     *
     * EndPoint - PUT : /api/identidad/updatepassword/{id}
     *
     * @param actionController el controlador de acción.
     */
    protected void apiUpdatePasswordUser(ActionController actionController)
    {
        // Se comprueba que el usuario está logueado y sea administrador
        if (!UserSession.isUserLogIn(actionController.server(), false))
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        // Se llama al servicio para procese la acción requerida
        identidadServicio.updatePasswordUser(actionController);
    }

    /**
     * Maneja la solicitud para crear un nuevo usuario.
     *
     * EndPoint - POST : /api/identidad/create
     *
     * @param actionController el controlador de acción.
     */
    protected void apiCreateUser(ActionController actionController)
    {
        // Se llama al servicio para procese la acción requerida
        identidadServicio.createUser(actionController);
    }

    /**
     * Maneja la solicitud para recuperar el password de un usuario.
     *
     * EndPoint - POST : /api/identidad/recovery
     *
     * @param actionController el controlador de acción.
     */
    protected void apiRecovery(ActionController actionController)
    {
        // Se llama al servicio para procese la acción requerida
        identidadServicio.recovery(actionController);
    }

    /**
     * Maneja las solicitudes GET.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);

        // Dependiendo del ActionType, realizará una acción
        switch(actionController.actionType()){
            case ActionType.LOGOUT :
                apiLogOut(actionController);
                break;

            case ActionType.USER:
                apiGetUser(actionController);
                break;

            case ActionType.USERS:
                apiGetUsers(actionController);
                break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }

    /**
     * Maneja las solicitudes POST.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint POST : " + actionController.parametros()[0]);

        // Dependiendo del ActionType, realizará una acción
        switch(actionController.actionType()){

            case ActionType.LOGIN :
                apiLogIn(actionController);
                break;

            case ActionType.CREATE :
                apiCreateUser(actionController);
                break;

            case ActionType.RECOVERY:
                apiRecovery(actionController);
            break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }

    /**
     * Maneja las solicitudes PUT.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
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

        // Dependiendo del ActionType, realizará una acción
        switch(actionController.actionType()){
            case ActionType.DELETE :
                apiDeleteUser(actionController);
                break;

            case ActionType.UPDATE :
                apiUpdateUser(actionController);
                break;

            case ActionType.UPDATEPASSWORD :
                apiUpdatePasswordUser(actionController);
                break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }

}
