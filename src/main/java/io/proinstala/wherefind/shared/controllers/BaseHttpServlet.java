package io.proinstala.wherefind.shared.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public class BaseHttpServlet extends HttpServlet {

    protected String getAction(HttpServletRequest request, String baseApi)
    {
        String accion = request.getRequestURI();
        accion = accion.replace(request.getContextPath(), "");
        accion = accion.replace(baseApi, "");
        return accion;
    }
}
