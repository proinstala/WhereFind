package io.proinstala.wherefind.api.identidad.services;

import java.util.ArrayList;
import java.util.List;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
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
            // Si la imagen de usuario está vacía se le añade la imagen por defecto
            if(userDTO.getImagen() == null || (userDTO.getImagen() != null && userDTO.getImagen().isBlank()))
            {
                userDTO.setImagen("App/img/defaultUser.svg");
            }

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
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1)
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }
        else
        {
            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id == -1)
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
            else
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
            }
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
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1)
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }
        else
        {

            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            if (id == -1)
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
            else
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
                }
            }
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
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1)
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }
        else
        {
            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
            if (id == -1)
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
            else
            {
                // Conecta con el Gestor de Permanencia
                IUserService userService = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userDTO = userService.getUserById(id);

                // Si el usuario no es nulo
                if (userDTO != null)
                {
                    // Obtiene los parámetros desde el request
                    String nombreUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_NOMBRE, userDTO.getNombre());
                    String apellidosUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_APELLIDOS, userDTO.getApellidos());
                    String emailUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_EMAIL, userDTO.getEmail());
                    String imagenUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_IMAGEN, userDTO.getImagen());

                    // Actualiza los datos del usuario con los pasados por el navegador
                    userDTO.setNombre(nombreUsuario);
                    userDTO.setApellidos(apellidosUsuario);
                    userDTO.setEmail(emailUsuario);
                    userDTO.setImagen(imagenUsuario);

                    try {
                        // Se guardan los cambios del usuario
                        if (userService.update(userDTO))
                        {
                            //Se vacía el password por motivos de seguridad
                            userDTO.setPassword("");

                            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_USER, userDTO, 0);

                            // Comprueba que el usuario logueado que esta editando al usuario sea el mismo que el usuario editado
                            // Si el id coincide (por ejemplo si se es administrador se pueden editar usuarios y el id no debería coincidir si no se esta editando a si mismo)
                            UserDTO userLogeado = UserSession.getUserLogin(actionController.server().request());
                            if (userLogeado.getId() == id)
                            {
                                // Guarda los datos modificados por el usuario en el user de la sessión, así si se pulsa F5,
                                // se cargan los datos actualizados del mismo
                                UserSession.setUserSession(userDTO, actionController.server());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     */
    public void updatePasswordUser(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1)
        {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        }
        else
        {
            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
            if (id == -1)
            {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
            else
            {
                // Conecta con el Gestor de Permanencia
                IUserService userService = GestionPersistencia.getUserService();

                // Obtiene los datos del usuario
                UserDTO userDTO = userService.getUserById(id);

                // Si el usuario no es nulo
                if (userDTO != null)
                {
                    // Obtiene los parámetros desde el request
                    String passwordUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, "");
                    String nuevoPassword = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_NUEVO_PASSWORD, "");
                    
                    boolean passwordOK = false;
                    if(passwordUsuario.equals(userDTO.getPassword()) && nuevoPassword.length() > 3 && nuevoPassword.length() < 61 && !nuevoPassword.equals(userDTO.getPassword())) {
                        // Actualiza los datos del usuario con los pasados por el navegador
                        userDTO.setPassword(nuevoPassword);
                        passwordOK = true;
                    }

                    try {
                        // Se guardan los cambios del usuario
                        if (passwordOK && userService.updatePasswordUser(userDTO))
                        {
                            //Se vacía el password por motivos de seguridad
                            userDTO.setPassword("");

                            // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_USER, userDTO, 0);

                            // Comprueba que el usuario logueado que esta editando al usuario sea el mismo que el usuario editado
                            // Si el id coincide (por ejemplo si se es administrador se pueden editar usuarios y el id no debería coincidir si no se esta editando a si mismo)
                            UserDTO userLogeado = UserSession.getUserLogin(actionController.server().request());
                            if (userLogeado.getId() == id)
                            {
                                // Guarda los datos modificados por el usuario en el user de la sessión, así si se pulsa F5,
                                // se cargan los datos actualizados del mismo
                                UserSession.setUserSession(userDTO, actionController.server());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
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
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Obtiene los parámetros desde el request
        String nombreUsuario       = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario     = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_PASSWORD, "");
        String nombreRealUsuario   = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_NOMBRE, "");
        String apellidoRealUsuario = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_APELLIDOS, "");
        String emailUsuario        = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_EMAIL, "");
        String imagenUsuario        = actionController.server().getRequestParameter(ConstParametros.PARAM_USUARIO_IMAGEN, "");

        // Comprueba que los datos del usuario no estén vacios
        if (!nombreUsuario.isBlank() && !passwordUsuario.isBlank() && passwordUsuario.length() > 3)
        {
            // Conecta con el Gestor de Permanencia
            IUserService userService = GestionPersistencia.getUserService();

            // Crea y guarda los datos del usuario
            UserDTO userDTO = null;
            try {
                userDTO = userService.add(new UserDTO(-1, nombreUsuario, passwordUsuario, "User", nombreRealUsuario, apellidoRealUsuario, emailUsuario, imagenUsuario));

            } catch (Exception ex) {
                ex.printStackTrace();

                if (userService.isGetStateEqualFromException(ex)) {
                    responseDTO = getResponseError("El nombre de usuario no es válido.");
                    responseJson(actionController.server().response(), responseDTO);
                    return;
                }
            }

            // Si el usuario no es nulo
            if (userDTO != null)
            {
                // Se vacía el password por motivos de seguridad
                userDTO.setPassword("");

                // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                responseDTO = getResponseOk(actionController.server().request().getContextPath() + "/index.jsp", userDTO, 1);
            }
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
}
