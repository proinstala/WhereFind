package io.proinstala.wherefind.identidad.services;

import java.util.ArrayList;
import java.util.List;
import io.proinstala.wherefind.identidad.UserSession;
import io.proinstala.wherefind.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.textos.ConstParametros;
import io.proinstala.wherefind.shared.textos.LocaleApp;

/**
 * Clase que maneja operaciones relacionadas con la identidad de los usuarios.
 */
public class IdentidadService extends BaseService {

    /**
     * Realiza el cierre de sesión del usuario.
     *
     * @param server Acción servidor
     */
    public void logOut(ActionServer server)
    {
        UserSession.logOut(server);
    }

    /**
     * Inicia la sesión con el usuario especificado.
     *
     * @param server Acción servidor
     */
    public void logIn(ActionServer server)
    {
        // Obtiene los parámetros desde el request
        String nombreUsuario   = server.getRequestParameter(ConstParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario = server.getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, "");

        // Intenta realizar el login con los datos pasados por el navegador
        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, server);

        // Respuesta de la acción actual
        ResponseDTO response;

        // Si el usuario existe
        if (usuario != null)
        {
            // Se vacía el password por motivos de seguridad
            usuario.setPassword("");

            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
            response = getResponseOk(server.request().getContextPath() + "/index.jsp", usuario, 1);
        }
        else
        {
            // El usuario no existe o se ha producido un error
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_USUARIO_NO_ENCONTRADO);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(server.response(), response);
    }


    /**
     * Recupera los datos de un usuario por ID y devuelve una respuesta OK si lo encuentra,
     * o un mensaje de error en caso contrario.
     *
     * @param actionController Controlador de acción
     */
    public void getUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService gestor = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userActual = gestor.getUserById(id);

                // Si el usuario recuperado no es nulo
                if (userActual != null)
                {
                    // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    response = getResponseOk("OK", userActual, 0);
                }
                else
                {
                    // Crea la respuesta con un error
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }

    /**
     * Devuelve la lista de usuarios almacenados en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void getUsers(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Conecta con el Gestor de Permanencia
        IUserService gestor = GestionPersistencia.getUserService();

        // Obtiene la lista de usuarios
        List<UserDTO> listado = gestor.getAllUsers();

        // Si la lista devuelta no es nula
        if (listado != null)
        {
            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
            response = getResponseOk("OK", listado, 0);
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void deleteUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService gestor = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userActual = gestor.getUserById(id);

                // Si el usuario no es nulo
                if (userActual != null)
                {
                    // Se vacía el password por motivos de seguridad
                    userActual.setPassword("");

                    // Se elimina el usuario
                    if (gestor.delete(userActual))
                    {
                        // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                        response = getResponseOk(LocaleApp.INFO_DELETE_USER, userActual, 0);
                    }
                    else
                    {
                        // Crea la respuesta con un error
                        response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    // Crea la respuesta con un error
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void updateUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService gestor = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userActual = gestor.getUserById(id);

                // Si el usuario no es nulo
                if (userActual != null)
                {
                    // Obtiene los parámetros desde el request
                    String passwordUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, userActual.getPassword());
                    String rolUsuario      = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_ROL, userActual.getRol());

                    // Actualiza los datos del usuario con los pasados por el navegador
                    userActual.setPassword(passwordUsuario);
                    userActual.setRol(rolUsuario);

                    // Se guardan los cambios del usuario
                    if (gestor.update(userActual))
                    {
                        // Se vacía el password por motivos de seguridad
                        userActual.setPassword("");

                        // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                        response = getResponseOk(LocaleApp.INFO_UPDATE_USER, userActual, 0);
                    }
                    else
                    {
                        // Crea la respuesta con un error
                        response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    // Crea la respuesta con un error
                    response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                response = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void createUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO response;

        // Obtiene los parámetros desde el request
        String nombreUsuario       = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario     = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, "");
        String nombreRealUsuario   = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_NOMBRE, "");
        String apellidoRealUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_APELLIDOS, "");
        String emailUsuario        = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_EMAIL, "");

        // Comprueba que los datos del usuario no estén vacios
        if (nombreUsuario != "" && passwordUsuario != "" )
        {
            // Conecta con el Gestor de Permanencia
            IUserService gestor = GestionPersistencia.getUserService();

            // Crea y guarda los datos del usuario
            UserDTO userActual = gestor.add(new UserDTO(-1, nombreUsuario, passwordUsuario, "User", nombreRealUsuario, apellidoRealUsuario, emailUsuario));

            // Si el usuario no es nulo
            if (userActual != null)
            {
                // Se vacía el password por motivos de seguridad
                userActual.setPassword("");

                // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                response = getResponseOk(actionController.server().request().getContextPath() + "/index.jsp", userActual, 1);
            }
            else
            {
                // Crea la respuesta con un error
                response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        }
        else
        {
            // Crea la respuesta con un error
            response = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), response);
    }
}
