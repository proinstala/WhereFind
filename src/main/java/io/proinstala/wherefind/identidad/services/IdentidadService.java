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
import io.proinstala.wherefind.shared.textos.LocaleApp;

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
        String nombreUsuario   = server.getRequestParameter("nombreUsuario", "");
        String passwordUsuario = server.getRequestParameter("passwordUsuario", "");

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
            response = getResponseError(LocaleApp.ERROR_USUARIO_NO_ENCONTRADO);
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
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
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
            response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
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
                        response = getResponseOk(LocaleApp.INFO_DELETE_USER, userActual);
                    }
                    else
                    {
                        response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
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
                        response = getResponseOk(LocaleApp.INFO_UPDATE_USER, userActual);
                    }
                    else
                    {
                        response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        responseJson(actionController.server().response(), response);
    }

    public void createUser(ActionController actionController)
    {
        ResponseDTO response;

        String nombreUsuario   = actionController.server().getRequestParameter("nombreUsuario", "");
        String passwordUsuario = actionController.server().getRequestParameter("passwordUsuario", "");


        if (nombreUsuario != "" && passwordUsuario != "" )
        {
            // Conecta con el Gestor de Permanencia
            IUserService gestor = GestionPersistencia.getUserService();

            // Crea los datos del usuario
            UserDTO userActual = gestor.add(new UserDTO(-1, nombreUsuario, passwordUsuario, "User"));

            if (userActual != null)
            {
                userActual.setPassword("");
                response = getResponseOk(LocaleApp.INFO_CREATE_USER, userActual);
            }
            else
            {
                response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        }
        else
        {
            response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), response);
    }
}
