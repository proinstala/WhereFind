package io.proinstala.wherefind.shared.controllers.actions;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record ActionServer(HttpServletRequest request, HttpServletResponse response) {

    public String getRequestParameter(String name, String porDefecto)
    {
        Map<String, String[]> parametros = request.getParameterMap();
        if (parametros.containsKey(name))
        {
            return parametros.get(name)[0];
        }

        return porDefecto;
    }


}
