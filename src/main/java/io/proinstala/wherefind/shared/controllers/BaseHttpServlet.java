package io.proinstala.wherefind.shared.controllers;

import java.io.IOException;

import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Clase BaseHttpServlet que extiende HttpServlet y proporciona métodos comunes para manejar peticiones HTTP.
 */
public class BaseHttpServlet extends HttpServlet {

    /**
     * Obtiene la acción a realizar a partir de la solicitud HTTP y la API base.
     *
     * @param request la solicitud HTTP recibida.
     * @param baseApi la ruta base de la API.
     * @return la acción que se debe realizar.
     */
    protected String getAction(HttpServletRequest request, String baseApi)
    {
        // Obtiene la url completa del request actual
        String accion = request.getRequestURI();

        // Se elimina la parte de la url que contiene getContextPath()
        accion = accion.replace(request.getContextPath(), "");

        // Se elimina la parte de la url que contiene baseApi
        accion = accion.replace(baseApi, "");

        // Devuelve el resultado
        return accion;
    }

    /**
     * Obtiene la ruta base de la API.
     *
     * @return la ruta base de la API.
     */
    protected String getBaseApi()
    {
        return "";
    }

    /**
     * Obtiene el tipo de acción a partir del nombre de acción.
     *
     * @param action el nombre de la acción.
     * @return el tipo de acción asociado.
     */
    protected Object getActionType(String action)
    {
        return action;
    }

    /**
     * Crea y devuelve un objeto ActionController para manejar la acción HTTP.
     *
     * @param request  la solicitud HTTP recibida.
     * @param response la respuesta HTTP que se enviará.
     * @return un objeto ActionController configurado para la acción recibida.
     */
    protected ActionController getActionController(HttpServletRequest request, HttpServletResponse response)
    {
        // Obtiene la url completa del request actual
        String fullUri       = request.getRequestURI();

        // Obtiene la parte de la api, limpiando los datos que no necesitamos
        String uriApi        = getAction(request, getBaseApi());

        // Obtine los parámetros usados en la api
        String[]  parametros = uriApi.replaceFirst("/", "").split("/");

        // Obtine el ActionType del actual request
        Object actionType = (parametros.length > 0) ? getActionType(parametros[0]) : getActionType("");

        // Crea el ActionController a partir de los datos extraidos anteriormente
        return new ActionController(fullUri, uriApi, actionType, parametros, new ActionServer(request, response));
    }

    /**
     * Envía una respuesta de error con el código de error y mensaje especificados.
     *
     * @param response la respuesta HTTP a la que se enviará el error.
     * @param error    el código de error HTTP.
     * @param mensaje  el mensaje de error asociado.
     */
    protected void responseError(HttpServletResponse response, int error, String mensaje)
    {
        try
        {
            response.sendError(error, mensaje);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Envía una respuesta de error 403 (Forbidden) con el mensaje especificado.
     *
     * @param response la respuesta HTTP a la que se enviará el error.
     * @param mensaje  el mensaje de error asociado.
     */
    protected void responseError403(HttpServletResponse response, String mensaje)
    {
        responseError(response, HttpServletResponse.SC_FORBIDDEN, mensaje);
    }

    /**
     * Envía una respuesta de error 404 (Not Found) con el mensaje especificado.
     *
     * @param response la respuesta HTTP a la que se enviará el error.
     * @param mensaje  el mensaje de error asociado.
     */
    protected void responseError404(HttpServletResponse response, String mensaje)
    {
        responseError(response, HttpServletResponse.SC_NOT_FOUND, mensaje);
    }
}