package io.proinstala.wherefind.identidad.services;

import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IdentidadService extends BaseService {

    public void logOut(HttpServletRequest request, HttpServletResponse response)
    {
        UserSession.logOut(request, response);
    }

    public void logIn(HttpServletRequest request, HttpServletResponse response)
    {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String passwordUsuario = request.getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, request);

        String resultado;

        if (usuario != null)
        {
            resultado = "{\"iserror\":0,\"result\":\""+ request.getContextPath()+"/index.jsp" + "\"}";
        }
        else
        {
            resultado = "{\"iserror\":1,\"result\":\"Usuario no encontrado o los datos introducidos son incorrectos.\"}";
        }

        responseJson(response, resultado);
    }


}
