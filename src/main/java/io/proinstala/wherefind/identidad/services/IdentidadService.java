package io.proinstala.wherefind.identidad.services;

import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IdentidadService extends BaseService {

    //Registro (record) para representar la estructura de la respuesta JSON
    record ResponseDTO(int isError, String result, Object user) {}

    public void logOut(ActionServer server)
    {
        UserSession.logOut(server.request(), server.response());
    }

    public void logIn(ActionServer server)
    {
        String nombreUsuario = server.request().getParameter("nombreUsuario");
        String passwordUsuario = server.request().getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, server.request());

        Gson gson = new GsonBuilder().create();
        String resultado;

        if (usuario != null)
        {
            //resultado = "{\"iserror\":0,\"result\":\""+ request.getContextPath()+"/index.jsp" + "\"}";
            resultado = gson.toJson(new ResponseDTO(0, server.request().getContextPath() + "/index.jsp", usuario));

        }
        else
        {
            //resultado = "{\"iserror\":1,\"result\":\"Usuario no encontrado o los datos introducidos son incorrectos.\"}";
            resultado = gson.toJson(new ResponseDTO(1, "Usuario no encontrado o los datos introducidos son incorrectos.", new UserDTO()));
        }

        responseJson(server.response(), resultado);
    }

}
