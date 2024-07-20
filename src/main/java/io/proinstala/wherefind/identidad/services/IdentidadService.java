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
        UserDTO userDTO = UserSession.login(nombreUsuario, passwordUsuario, server);

        // Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Si el usuario existe
        if (userDTO != null)
        {
            // Se vacía el password por motivos de seguridad
            userDTO.setPassword("");

            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
            responseDTO = getResponseOk(server.request().getContextPath() + "/index.jsp", userDTO, 1);
        }
        else
        {
            // El usuario no existe o se ha producido un error
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_USUARIO_NO_ENCONTRADO);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(server.response(), responseDTO);
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
        ResponseDTO responseDTO;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService userService = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userDTO = userService.getUserById(id);

                // Si el usuario recuperado no es nulo
                if (userDTO != null)
                {
                    // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk("OK", userDTO, 0);
                }
                else
                {
                    // Crea la respuesta con un error
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Devuelve la lista de usuarios almacenados en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void getUsers(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Permanencia
        IUserService userService = GestionPersistencia.getUserService();

        // Obtiene la lista de usuarios
        List<UserDTO> listaUserDTO = userService.getAllUsers();

        // Si la lista devuelta no es nula
        if (listaUserDTO != null)
        {
            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
            responseDTO = getResponseOk("OK", listaUserDTO, 0);
        }
        else
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void deleteUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService userService = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userDTO = userService.getUserById(id);

                // Si el usuario no es nulo
                if (userDTO != null)
                {
                    // Se vacía el password por motivos de seguridad
                    userDTO.setPassword("");

                    // Se elimina el usuario
                    if (userService.delete(userDTO))
                    {
                        // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                        responseDTO = getResponseOk(LocaleApp.INFO_DELETE_USER, userDTO, 0);
                    }
                    else
                    {
                        // Crea la respuesta con un error
                        responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    // Crea la respuesta con un error
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void updateUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length > 1) {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id > -1)
            {
                // Conecta con el Gestor de Permanencia
                IUserService userService = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userDTO = userService.getUserById(id);

                // Si el usuario no es nulo
                if (userDTO != null)
                {
                    // Obtiene los parámetros desde el request
                    String passwordUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, userDTO.getPassword());
                    String rolUsuario      = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_ROL, userDTO.getRol());

                    // Actualiza los datos del usuario con los pasados por el navegador
                    userDTO.setPassword(passwordUsuario);
                    userDTO.setRol(rolUsuario);

                    // Se guardan los cambios del usuario
                    if (userService.update(userDTO))
                    {
                        // Se vacía el password por motivos de seguridad
                        userDTO.setPassword("");

                        // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                        responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_USER, userDTO, 0);
                    }
                    else
                    {
                        // Crea la respuesta con un error
                        responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                    }
                }
                else
                {
                    // Crea la respuesta con un error
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }
            }
            else
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        }
        else
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void createUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Obtiene los parámetros desde el request
        String nombreUsuario       = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario     = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, "");
        String nombreRealUsuario   = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_NOMBRE, "");
        String apellidoRealUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_APELLIDOS, "");
        String emailUsuario        = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_EMAIL, "");
        String imagenUsuario        = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_IMAGEN, "");

        // Comprueba que los datos del usuario no estén vacios
        if (!nombreUsuario.isBlank() && !passwordUsuario.isBlank())
        {
            // Conecta con el Gestor de Permanencia
            IUserService userService = GestionPersistencia.getUserService();

            // Crea y guarda los datos del usuario
            UserDTO userDTO = userService.add(new UserDTO(-1, nombreUsuario, passwordUsuario, "User", nombreRealUsuario, apellidoRealUsuario, emailUsuario, imagenUsuario));

            // Si el usuario no es nulo
            if (userDTO != null)
            {
                // Se vacía el password por motivos de seguridad
                userDTO.setPassword("");

                // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                responseDTO = getResponseOk(actionController.server().request().getContextPath() + "/index.jsp", userDTO, 1);
            }
            else
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        }
        else
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
