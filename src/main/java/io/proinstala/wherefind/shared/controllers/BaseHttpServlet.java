package io.proinstala.wherefind.shared.controllers;

import java.io.IOException;

import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BaseHttpServlet extends HttpServlet {

    protected String getAction(HttpServletRequest request, String baseApi)
    {
        String accion = request.getRequestURI();
        accion = accion.replace(request.getContextPath(), "");
        accion = accion.replace(baseApi, "");
        return accion;
    }

    protected String getBaseApi()
    {
        return "";
    }

    protected Object getActionType(String action)
    {
        return action;
    }

    protected ActionController getActionController(HttpServletRequest request, HttpServletResponse response)
    {
        String fullUri       = request.getRequestURI();
        String uriApi        = getAction(request, getBaseApi());
        String[]  parametros = uriApi.replaceFirst("/", "").split("/");

        System.out.println("-----------------------------------------");
        for (String item : parametros) {
            System.out.println("parametros : " + item);
        }
        System.out.println("-----------------------------------------");

        Object actionType = (parametros.length > 0) ? getActionType(parametros[0]) : getActionType("");

        System.out.println("Tipo : " + actionType);

        return new ActionController(fullUri, uriApi, actionType, parametros, new ActionServer(request, response));
    }

    protected void responseError403(HttpServletResponse response, String mensaje)
    {
        //response.setHeader("X-Error-Message", mensaje);
        try
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, mensaje);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
