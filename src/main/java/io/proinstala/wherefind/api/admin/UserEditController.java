package io.proinstala.wherefind.api.admin;
import java.io.IOException;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Clase servlet que administra las peticiones HTTP relacionadas con la edición de usuarios en modo administrador.
 */
@WebServlet(urlPatterns = "/admin/users/edit/*")
public class UserEditController extends BaseHttpServlet{

    /**
     * Base de la URL para las API de administracion.
     */
    protected static final String BASE_URL_RECOVERY = "/admin/users";

    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        EDIT
    }

    /**
     * Obtiene la base de la URL de la API.
     *
     * @return la base de la URL de la API.
     */
    @Override
    protected String getBaseApi()
    {
        return BASE_URL_RECOVERY;
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
     * Maneja la solicitud para editar los datos de un usuario por un administrador.
     *
     * @param actionController el controlador de acción.
     */
    protected void editarUsuarioByAdmin(ActionController actionController)
    {
        if(UserSession.redireccionarIsUserNotLogIn(actionController.server())){
            // Detiene la ejecución de este servlet
            return;
        }

        // Si el usuario está logueado pero no es administrado
        if (!UserSession.isUserLogIn(actionController.server(), true))
        {
            // Obtiene un error 403
            BaseHttpServlet.responseError403(actionController.server().response(), "");
            return;
        }

        boolean isError = false;

        // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
        int id = actionController.getIntFromParametros(1);

        if (id == -1)
        {
            // Obtiene un error 403
            isError = true;
        }

        if(isError)
        {
            // Obtiene un error 403
            BaseHttpServlet.responseError403(actionController.server().response(), "");
            return;
        }

        // Se establece un atributo en la solicitud actual con los datos del usuario recuperado que serán accesibles para la vista correspondiente
        actionController.server().request().setAttribute("userIdByAdmin", id);
        actionController.server().request().setAttribute("isEditarByAdmin", true);

        try
        {
            // Se dirige a una página JSP para finalizar el proceso de recuperación de contraseña, manejada por el dispatch del servidor.
            actionController.server().request().getRequestDispatcher("/App/web/identidad/modificarUsuario.jsp").forward(actionController.server().request(), actionController.server().response());
        }
        catch (ServletException | IOException ex)
        {
            ex.printStackTrace();
        }
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

        // Se desactiva la cache del navegador para esta página
        UserSession.disableCacheWebBrowser(actionController.server());

        // Imprime en la salida del servidor el EndPoint
        System.out.println("ACTION : " +   actionController.actionType());

        // Dependiendo del ActionType, realizará una acción
        switch(actionController.actionType()){

            case ActionType.EDIT :
                editarUsuarioByAdmin(actionController);
            break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }

}
