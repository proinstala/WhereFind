package io.proinstala.wherefind.api.identidad.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.proinstala.wherefind.api.identidad.UserSession;
import io.proinstala.wherefind.api.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.api.infraestructure.email.Email;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlApp;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import io.proinstala.wherefind.shared.services.BaseService;


/**
 * Clase que maneja operaciones relacionadas con la identidad de los usuarios.
 */
public class IdentidadControllerService extends BaseService {

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
        String nombreUsuario   = server.getRequestParameter(FormParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario = server.getRequestParameter(FormParametros.PARAM_USUARIO_PASSWORD, "");

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
            responseDTO = getResponseOk(server.request().getContextPath() + "/" + UrlApp.HOME.getUri(), userDTO, 1);
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
                    String nombreUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_NOMBRE, userDTO.getNombre());
                    String apellidosUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_APELLIDOS, userDTO.getApellidos());
                    String emailUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_EMAIL, userDTO.getEmail());
                    String imagenUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_IMAGEN, userDTO.getImagen());

                    if (isValidParametro(nombreUsuario, 1, 100)
                        && isValidParametro(apellidosUsuario, 1, 100)
                        && isValidParametro(emailUsuario, 1, 200))
                    {
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
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }


    /**
     * Actualiza los datos de un usuario en la base de datos.
     *
     * @param actionController Controlador de acción
     * @param isReset
     */
    public void updatePasswordUser(ActionController actionController, boolean isReset)
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
                    String passwordUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_PASSWORD, "");
                    String nuevoPassword = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_NUEVO_PASSWORD, "");
                    String confirmPassword = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_CONFIRMACION_PASSWORD, "");

                    boolean passwordOK = false;

                    if (!isReset)
                    {
                        if(passwordUsuario.equals(userDTO.getPassword()) && isValidConfirmPassword(nuevoPassword, confirmPassword) && !nuevoPassword.equals(userDTO.getPassword()) ) {
                            // Actualiza los datos del usuario con los pasados por el navegador
                            userDTO.setPassword(nuevoPassword);
                            passwordOK = true;
                        }
                    }
                    else
                    {
                        if(isValidConfirmPassword(nuevoPassword, confirmPassword)) {
                            // Actualiza los datos del usuario con los pasados por el navegador
                            userDTO.setPassword(nuevoPassword);
                            passwordOK = true;
                        }
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
                            if (userLogeado != null && userLogeado.getId() == id)
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
        String nombreUsuario       = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_USERNAME, "");
        String passwordUsuario     = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_PASSWORD, "");
        String nombreRealUsuario   = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_NOMBRE, "");
        String apellidoRealUsuario = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_APELLIDOS, "");
        String emailUsuario        = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_EMAIL, "");
        String imagenUsuario        = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_IMAGEN, "");

        // Comprueba que los datos del usuario no estén vacios
        if(isValidParametro(nombreUsuario, 1, 100)
            && isValidPassword(passwordUsuario)
            && isValidParametro(nombreRealUsuario, 1, 100)
            && isValidParametro(apellidoRealUsuario, 1, 100)
            && isValidParametro(emailUsuario, 1, 200))
        {
            // Conecta con el Gestor de Permanencia
            IUserService userService = GestionPersistencia.getUserService();

            // Crea y guarda los datos del usuario
            UserDTO userDTO = null;
            try {
                userDTO = userService.add(new UserDTO(-1, nombreUsuario, passwordUsuario, "User", nombreRealUsuario, apellidoRealUsuario, emailUsuario, imagenUsuario, true));

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
                responseDTO = getResponseOk(actionController.server().request().getContextPath() + "/" + UrlApp.HOME.getUri(), userDTO, 1);
            }
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }


    /**
     * Envía un email al email del usuario para poder resetear el password
     *
     * @param actionController Controlador de acción
     */
    public void recovery(ActionController actionController)
    {
        // Respuesta de la acción actual
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);

        // Obtiene los parámetros desde el request
        String nombreUsuario       = actionController.server().getRequestParameter(FormParametros.PARAM_USUARIO_USERNAME, "");

        // Comprueba que los datos del usuario no estén vacios
        if (!nombreUsuario.isBlank())
        {
            // Conecta con el Gestor de Permanencia
            IUserService userService = GestionPersistencia.getUserService();

            // Busca al usuario por el nombre de usuario o email
            UserDTO userDTO = null;
            try
            {
                userDTO = userService.getUserByUserNameOrEmail(nombreUsuario);

                if (!recoverySendEmailToUser(userDTO, actionController.getServerUrlBase()))
                {
                    userDTO = null;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();

                responseDTO = getResponseError("El nombre de usuario o el email no son válidos.");
                responseJson(actionController.server().response(), responseDTO);
                return;
            }

            // Si el usuario no es nulo
            if (userDTO != null)
            {
                // Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                responseDTO = getResponseOk(LocaleApp.EMAIL_CAMBIAR_PASSWORD_INFO, null, 0);
            }
        }

        // Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }


    /**
     * Método protegido para enviar un correo electrónico a un usuario cuando se recupera su contraseña.
     *
     * @param userDTO Información del usuario que se va a notificar sobre la restauración de su contraseña.
     * @param url La URL principal del sistema, necesaria para generar el enlace de recuperación.
     * @return true si se envía el correo electrónico correctamente, false en caso de error.
     */
    protected boolean recoverySendEmailToUser(UserDTO userDTO, String url)
    {
        long epochSeconds = Instant.now().getEpochSecond();

        // Generamos la URL para la página de recuperación
        String linkRecuperacion = url + "/"
                                + UrlIdentidad.RECOVERY.getUri()
                                + "/?email="
                                + userDTO.getEmail()
                                + "&time=" + epochSeconds
                                + "&hash=" + recoveryGetHashId(recoveryGetTextoId(userDTO.getEmail(), epochSeconds));

        // Generamos el enlace HTML para la recuperación
        String enlaceRecuperacion = String.format("<a href='%s'>%s</a>", linkRecuperacion, linkRecuperacion);

        // Preparamos el mensaje del correo electrónico
        String mensajeEmail = String.format(LocaleApp.EMAIL_CAMBIAR_PASSWORD_CUERPO, userDTO.getUserName(), enlaceRecuperacion);

        // Enviamos el correo electrónico al usuario
        return Email.enviarEmail(userDTO.getEmail(), LocaleApp.EMAIL_CAMBIAR_PASSWORD_TITULO, mensajeEmail);
    }


    /**
     * Genera un hash SHA-1 a partir de la cadena dada como parámetro.
     *
     * @param input Cadena a procesar para generar el hash
     * @return String representación hexadecimal del hash SHA-1
     */
    protected String recoveryGetHashId(String input)
    {
        // Crea un nuevo hash utilizando algoritmo SHA-1
        MessageDigest sha1 = null;
        try
        {
            // Crea un nuevo hash utilizando algoritmo SHA-1
            sha1 = MessageDigest.getInstance("SHA-1");  // Crea un nuevo hash utilizando SHA-1

            // Convierte la cadena de entrada a bytes y calcula el mensaje digest
            byte[] messageDigest = sha1.digest(input.getBytes());

            // Convierte los bytes del mensaje digest a un número big integer hexadecimal
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);  // Convertir el arreglo de bytes a hexadecimal

            // Completar con ceros para lograr un hash de 32 caracteres
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        // Devuelve un string vacío en caso de error
        return "";
    }


    /**
     * Metodo para realizar la recuperación del ID de texto a partir de los parámetros.
     *
     * @param input El texto que se va a procesar.
     * @param time La marca de tiempo asociada al proceso.
     * @return Un string con el texto convertido a minúsculas y la marca de tiempo.
     */
    protected String recoveryGetTextoId(String input, long time)
    {
        return input.toLowerCase() + "---> [" + time + "]";
    }


    /**
     * Método para la recuperación de usuario verificado.
     *
     * @param actionController Controlador de acción que maneja las peticiones del cliente.
     * @return Devuelve un UserDTO con los datos del usuario recién recuperados, vacío en caso contrario.
     */
    public UserDTO recoveryVerificar(ActionController actionController)
    {
        // Inicializar usuario DTO a null
        UserDTO userDTO = null;

        // Obtener parámetros de la petición: 'email', 'time' y 'hash'
        String email = actionController.server().getRequestParameter("email", "");
        String time  = actionController.server().getRequestParameter("time", "");
        String hash  = actionController.server().getRequestParameter("hash", "");

        // Si alguno de los parámetros está vacío, se devuelve usuario DTO vacío
        if (email.isBlank() || time.isBlank() || hash.isBlank())
        {
            return userDTO;
        }

        // Convertir 'time' en segundos desde la época (1970-01-01T00:00:00Z)
        long epochSeconds = Long.parseLong(time);

        // Calcular hash a partir del texto obtenido del email y tiempo con el método 'recoveryGetHashId'
        String hashCalculado = recoveryGetHashId(recoveryGetTextoId(email, epochSeconds));

        // Si los hashes no coinciden, se devuelve usuario DTO vacío
        if (!hash.equalsIgnoreCase(hashCalculado))
        {
            return userDTO;
        }

        // Verificar si el tiempo pasado es válido comparándolo con la hora actual en segundos
        // y devolviendo usuario DTO vacío en caso de que no lo sea.
        if (!verificarSegundos(Instant.now().getEpochSecond(), epochSeconds))
        {
            return userDTO;
        }

        // Verificar si el intento de recuperación es el primero
        if (!verificarIntento(hash))
        {
            return userDTO;
        }

        // Conectar con el Gestor de Permanencia y obtener UserService
        IUserService userService = GestionPersistencia.getUserService();

        // Consulta al usuario en la base de datos usando el email proporcionado,
        // almacenando los datos del usuario obtenidos en 'userDTO'
        userDTO = userService.getUserByUserNameOrEmail(email);

        // Devolver el objeto UserDTO con los campos requeridos del usuario recién recuperado
        return userDTO;
    }


    /**
     * Método que verifica si los segundos suministrados de acuerdo a dos valores dados cumplen una serie de condiciones.
     * Además, se verificará que el valorA no sea menor al del valorB y que la diferencia entre ambos valores esté en un máximo de 24 horas.
     *
     * @param valorA Valor a comparar contra el segundo valor proporcionado (fecha/hora)
     * @param valorB Segundo valor con respecto al cual se compara el primer valor (fecha/hora)
     * @return boolean True si se cumplen ambas condiciones, False de lo contrario
     */
    public static boolean verificarSegundos(long valorA, long valorB)
    {
        // Condición 1: A no puede ser menor que B
        if (valorA < valorB) {
            return false;
        }

        // Condición 2: Entre B y A no pueden haber pasado más de 24 horas
        long segundosEn24Horas = (long)24 * 60 * 60;
        if (valorA - valorB > segundosEn24Horas) {
            return false;
        }

        // Si ambas condiciones se cumplen, retorna true
        return true;
    }

    /**
     * Verifica si es válido el intento de recuperarcion de contraseña.
     *
     * @param hash el valor hasheado de la intención
     * @return {@code true} si la intentación es correcta, caso contrario {false}
     */
    public static boolean verificarIntento(String hash)
    {
        // Conectar con el Gestor de Permanencia y obtener UserService
        IUserService userService = GestionPersistencia.getUserService();

        // Consulta que el hash del recovery solo tenga 1 intento de recuperación
        return userService.getRecoveryIntentos(hash);
    }



    /**
     * Método para verificar la valides del password, devuelve true si se cumplen las condiciones (longitud entre 4 y 60 caracteres), caso contrario false.
     *
     * @param   password         La contraseña a validar
     * @return                  Boolean indicando si el password es válido o no.
     */
    public static boolean isValidPassword(String password)
    {
        return password.length() > 3 && password.length() < 61;
    }

    /**
     * Método para asegurar que los passwords proporcionados son iguales y válidos en comparación, devuelve true si se cumplen las condiciones, caso contrario false.
     *
     * @param   nuevoPassword     El primer password por confirmar
     * @param   confirmPassword  La segunda ocurrencia del password para confirmación
     * @return                   Boolean indicando si el password (confirm password) es válido y igual al primero.
     */
    public static boolean isValidConfirmPassword(String nuevoPassword, String confirmPassword)
    {
        return isValidPassword(nuevoPassword) && nuevoPassword.equals(confirmPassword);
    }

    /**
     * Método que verifica si un parámetro es válido según determinadas condiciones de longitud.
     *
     * @param parametro El string a validar. Debe ser no blanco, menor de maxSize y mayor que minSize caracteres.
     * @param minSize   Tamaño mínimo permitido para el parámetro.
     * @param maxSize   Tamaño máximo permitido para el parámetro.
     *
     * @return boolean Indica si el parámetros cumple las condiciones de longitud establecidas (no blanco y entre minSize y maxSize caracteres).
     */
    public static boolean isValidParametro(String parametro, int minSize, int maxSize)
    {
        return !parametro.isBlank() && parametro.length() >= minSize && parametro.length() <= maxSize;
    }


}
