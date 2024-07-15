package io.proinstala.wherefind.identidad.services;

import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
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
        UserSession.logOut(server);
    }

    public void logIn(ActionServer server)
    {
        String nombreUsuario = server.request().getParameter("nombreUsuario");
        String passwordUsuario = server.request().getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, server);

        Gson gson = new GsonBuilder().create();
        String resultado;

        if (usuario != null)
        {
            // Se vacía el password por motivos de seguridad
            usuario.setPassword("");
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

    public void getUser(ActionController actionController)
    {
        String resultado;

        Gson gson = new GsonBuilder().create();

        if (actionController.parametros().length > 1) {
            int id = actionController.getIntFromParametros(1);
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService gestor = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userActual = gestor.getUserById(id);

                if (userActual != null)
                {
                    resultado = gson.toJson(new ResponseDTO(0, "OK.", userActual));
                }
                else
                {
                    resultado = gson.toJson(new ResponseDTO(1, "Se ha producido un error.", new UserDTO()));
                }
            }
            else
            {

                resultado = gson.toJson(new ResponseDTO(1, "El parámetro no es correcto.", new UserDTO()));
            }
        }
        else
        {
            resultado = gson.toJson(new ResponseDTO(1, "Faltan parámetros para poder realizar la acción solicitada.", new UserDTO()));
        }

        responseJson(actionController.server().response(), resultado);
    }
}
