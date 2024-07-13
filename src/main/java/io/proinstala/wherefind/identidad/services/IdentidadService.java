package io.proinstala.wherefind.identidad.services;

import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IdentidadService extends BaseService {
    
    //Registro (record) para representar la estructura de la respuesta JSON
    record ResponseDTO(int isError, String result, Object user) {}

    public void logOut(HttpServletRequest request, HttpServletResponse response)
    {
        UserSession.logOut(request, response);
    }

    public void logIn(HttpServletRequest request, HttpServletResponse response)
    {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String passwordUsuario = request.getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, request);

        Gson gson = new GsonBuilder().create();
        String resultado;

        if (usuario != null)
        {
            //resultado = "{\"iserror\":0,\"result\":\""+ request.getContextPath()+"/index.jsp" + "\"}";
            resultado = gson.toJson(new ResponseDTO(0, request.getContextPath() + "/index.jsp", usuario));
            
        }
        else
        {
            //resultado = "{\"iserror\":1,\"result\":\"Usuario no encontrado o los datos introducidos son incorrectos.\"}";
            resultado = gson.toJson(new ResponseDTO(1, "Usuario no encontrado o los datos introducidos son incorrectos.", new UserDTO()));
        }

        responseJson(response, resultado);
    }

}
