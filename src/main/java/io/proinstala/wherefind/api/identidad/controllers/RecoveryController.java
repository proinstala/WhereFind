package io.proinstala.wherefind.api.identidad.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.identidad.services.IdentidadControllerService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Controlador del recovery del password
 */
@WebServlet(urlPatterns = "/account/recovery/*")
public class RecoveryController  extends BaseHttpServlet {

    /**
     * Base de la URL para las API de identidad.
     */
    protected static final String BASE_URL_RECOVERY = "/account";

    // Se declara el servicio que realmente gestiona todas las acciones relacionadas con la api
    private final IdentidadControllerService identidadServicio = new IdentidadControllerService();

    /**
     * Tipos de acción que este controlador puede manejar.
     */
        enum ActionType {
            ERROR,
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
     * Maneja la solicitud para recuperar el password de un usuario.
     *
     * @param actionController el controlador de acción.
     */
    protected void recoveryPassWord(ActionController actionController)
    {
        // Se intenta validar al usuario para recuperar su clave desde los datos presentados en el controlador de acciones.
        UserDTO userDTO = identidadServicio.recoveryVerificar(actionController);

        // Si no se logra verificar u obtener un usuario válido,
        // entonces se responde con un error de acceso prohibido al cliente (status HTTP:403) y finaliza este método.
        if (userDTO == null)
        {
            responseError403(actionController.server().response(), "");
            return;
        }

        // Se establece un atributo en la solicitud actual con los datos del usuario recuperado que serán accesibles para la vista correspondiente
        actionController.server().request().setAttribute("userDTO", userDTO);

        try
        {
            // Se dirige a una página JSP para finalizar el proceso de recuperación de contraseña, manejada por el dispatch del servidor.
            actionController.server().request().getRequestDispatcher("/App/web/identidad/recoveryPasswordFinal.jsp").forward(actionController.server().request(), actionController.server().response());
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

            case ActionType.RECOVERY :
                recoveryPassWord(actionController);
            break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }
}
