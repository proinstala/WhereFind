package io.proinstala.wherefind.identidad.services;

import java.util.ArrayList;
import java.util.List;

import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;

public class IdentidadService extends BaseService {

    //Registro (record) para representar la estructura de la respuesta JSON
    record ResponseDTO(int isError, String result, Object user) {}

    protected ResponseDTO createResponseDTO(int isError, String mensaje, Object objecto)
    {
        return new ResponseDTO(isError, mensaje, objecto);
    }

    protected ResponseDTO getResponseError(String mensaje)
    {
        return getResponseError(mensaje, new UserDTO());
    }

    protected ResponseDTO getResponseError(String mensaje, Object objecto)
    {
        return createResponseDTO(1, mensaje, objecto);
    }

    protected ResponseDTO getResponseOk(String mensaje, Object objecto)
    {
        return createResponseDTO(0, mensaje, objecto);
    }

    public void logOut(ActionServer server)
    {
        UserSession.logOut(server);
    }

    public void logIn(ActionServer server)
    {
        String nombreUsuario = server.request().getParameter("nombreUsuario");
        String passwordUsuario = server.request().getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, server);

        //String resultado;
        ResponseDTO response;

        if (usuario != null)
        {
            // Se vacía el password por motivos de seguridad
            usuario.setPassword("");
            response = getResponseOk(server.request().getContextPath() + "/index.jsp", usuario);

        }
        else
        {
            response = getResponseError("Usuario no encontrado o los datos introducidos son incorrectos.");
        }

        responseJson(server.response(), response);
    }


    public void getUser(ActionController actionController)
    {
        ResponseDTO response;

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
                    response = getResponseOk("OK", userActual);
                }
                else
                {
                    response = getResponseError("Se ha producido un error.");
                }
            }
            else
            {
                response = getResponseError("El parámetro no es correcto.");
            }
        }
        else
        {
            response = getResponseError("Faltan parámetros para poder realizar la acción solicitada.");
        }

        responseJson(actionController.server().response(), response);
    }

    public void getUsers(ActionController actionController)
    {
        ResponseDTO response;

        // Conecta con el Gestor de Permanencia
        IUserService gestor = GestionPersistencia.getUserService();

        // Obtiene la lista de usuarios
        List<UserDTO> listado = gestor.getAllUsers();

        if (listado != null)
        {
            response = getResponseOk("OK", listado);
        }
        else
        {
            response = getResponseError("Se ha producido un error.", new ArrayList<>());
        }

        responseJson(actionController.server().response(), response);
    }

    public void deleteUser(ActionController actionController)
    {
        ResponseDTO response;

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
                    userActual.setPassword("");

                    if (gestor.delete(userActual))
                    {
                        response = getResponseOk("Se ha eliminado el usuario correctamente", userActual);
                    }
                    else
                    {
                        response = getResponseError("Se ha producido un error.");
                    }
                }
                else
                {
                    response = getResponseError("Se ha producido un error.");
                }
            }
            else
            {
                response = getResponseError("El parámetro no es correcto.");
            }
        }
        else
        {
            response = getResponseError("Faltan parámetros para poder realizar la acción solicitada.");
        }

        responseJson(actionController.server().response(), response);
    }





    public void updateUser(ActionController actionController)
    {
        ResponseDTO response;

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
                    String passwordUsuario = actionController.server().getRequestParameter("passwordUsuario", userActual.getPassword());
                    String rolUsuario = actionController.server().getRequestParameter("rolUsuario", userActual.getRol());

                    userActual.setPassword(passwordUsuario);
                    userActual.setRol(rolUsuario);

                    if (gestor.update(userActual))
                    {
                        userActual.setPassword("");
                        response = getResponseOk("Se ha modificado el usuario correctamente", userActual);
                    }
                    else
                    {
                        response = getResponseError("Se ha producido un error.");
                    }
                }
                else
                {
                    response = getResponseError("Se ha producido un error.");
                }
            }
            else
            {
                response = getResponseError("El parámetro no es correcto.");
            }
        }
        else
        {
            response = getResponseError("Faltan parámetros para poder realizar la acción solicitada.");
        }

        responseJson(actionController.server().response(), response);
    }




}
