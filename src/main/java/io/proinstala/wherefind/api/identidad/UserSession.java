package io.proinstala.wherefind.api.identidad;

import java.io.IOException;

import io.proinstala.wherefind.api.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.consts.urls.UrlsInternas;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlApp;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que contiene métodos estáticos para manejar la sesión de un usuario.
 */
public class UserSession {

    /**
     * Realiza el proceso de login para un usuario.
     *
     * @param userName Nombre de usuario
     * @param password Contraseña del usuario
     * @param server Instancia de ActionServer
     * @return El objeto UserDTO del usuario autenticado o null si no se logueó correctamente
     */
    public static UserDTO login(String userName, String password, ActionServer server)
    {
        // Conecta con el Gestor de Permanencia
        IUserService userService = GestionPersistencia.getUserService();

        // Obtiene los datos del usuario
        UserDTO userDTO = userService.getUser(userName, password);

        // Estable en la session el usuario logueado
        setUserSession(userDTO, server);

        return userDTO;
    }


    /**
     * Establece la sesión del usuario utilizando la información proporcionada y el servidor de acción.
     *
     * Este método estático recibe un objeto {@code UserDTO} que contiene la información del usuario
     * y un objeto {@code ActionServer} que representa el servidor de acción. Realiza las operaciones
     * necesarias para establecer la sesión del usuario en el sistema utilizando ambos parámetros.
     *
     * @param userDTO el objeto {@code UserDTO} que contiene la información del usuario.
     * @param server  el objeto {@code ActionServer} que representa el servidor de acción.
     */
    public static void setUserSession(UserDTO userDTO, ActionServer server)
    {
        // Comprueba que el usuario no sea nulo
        if (userDTO != null)
        {
            // Recupera la sesión actual desde el request
            HttpSession session = server.request().getSession();

            // Guarda en la sesión actual el usuario logueado
            session.setAttribute("user", userDTO);
        }
    }


    /**
     * Realiza el proceso de logout para un usuario.
     *
     * @param server Instancia de ActionServer
     */
    public static void logOut(ActionServer server)
    {
        // Obtiene la sesión actual desde el request
        HttpSession session = server.request().getSession(false);

        // Si la sesión no es nula
        if (session != null)
        {
            // Elimina los datos del usuario actualmente logueado
            session.removeAttribute("user");

            // Invalida la sesión actual
            session.invalidate();
        }

        // Redirecciona al usuario a la página de login
        redireccionar(server.response(), server.request().getContextPath() + "/" + UrlsInternas.getAppdUri(UrlApp.HOME));
    }


    /**
     * Redirige al usuario a una página según sea necesario.
     *
     * @param server Instancia de ActionServer
     * @param uri Uri destino
     * @param uriNotLogin Uri alternativa en caso de que el usuario no esté logueado
     */
    public static void redireccionar(ActionServer server, String uri, String uriNotLogin)
    {
        // Si el usuario logueado es nulo la finalUri es igual a uriNotLogin, sino es igual a uri
        String finalUri = (getUserLogin(server.request()) == null) ? uriNotLogin : uri;

        // Redirecciona al usuario a la página contenida en finalUri
        redireccionar(server.response(), finalUri);
    }

    /**
     * Redirige al usuario a una página utilizando un objeto HttpServletResponse.
     *
     * @param response Objeto HttpServletResponse
     * @param uri Uri destino
     */
    public static void redireccionar(HttpServletResponse response, String uri)
    {
        try
        {
            // Redirecciona al usuario a la página contenida en uri
            response.sendRedirect(uri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene los datos del usuario logueado.
     *
     * @param request Instancia de HttpServletRequest
     * @return El objeto UserDTO del usuario logueado o null si no se logueó correctamente
     */
    public static UserDTO getUserLogin(HttpServletRequest request)
    {
        // Recupera la sesión actual desde el request
        HttpSession session = request.getSession();

        // Recupera los datos del usuario logueado
        return (UserDTO)session.getAttribute("user");
    }

    /**
     * Redirige al usuario a la página de login si no está logueado.
     *
     * @param server Instancia de ActionServer
     */
    public static boolean redireccionarIsUserNotLogIn(ActionServer server)
    {
        // Se desactiva la cache del navegador para esta página
        disableCacheWebBrowser(server);

        // Obtiene el UserDTO del usuario logueado
        UserDTO userDTO = getUserLogin(server.request());

        // Si el usuario no está logueado lo redirije a la página de login
        if (userDTO == null) {
            // Redirecciona al usuario a la página de login
            redireccionar(server.response(), server.request().getContextPath()+"/" + UrlsInternas.getIdentidadUri(UrlIdentidad.LOGIN));

            return true;
        }

        return false;
    }

    /**
     * Desactiva la cache del navegador.
     *
     * @param server Instancia de ActionServer
     */
    public static void disableCacheWebBrowser(ActionServer server)
    {
        // Añade las cabeceras necesarias al response para que la página actual no sea cacheada por el navegador del usuario.
        server.response().setHeader("Cache-Control", "no-cache");
        server.response().setHeader("Cache-Control", "no-store");
        server.response().setHeader("Pragma", "no-cache");
        server.response().setDateHeader("Expires", -1);
    }

    /**
     * Obtiene el nombre completo del usuario logueado.
     *
     * @param request Instancia de HttpServletRequest
     * @return El nombre completo del usuario logueado (nombre y apellidos)
     */
    public static String getLoginUserFullName(HttpServletRequest request)
    {
        // Obtiene el UserDTO del usuario logueado
        UserDTO userDTO = getUserLogin(request);

        // Si el usuario no es nulo
        if (userDTO != null) {
            // Devuelve el nombre completo del usuario logueado
            return userDTO.getNombre() + " " + userDTO.getApellidos();
        }

        // Devuelve No aplicable si no hay usuario logueado
        return "n/a";
    }

    /**
     * Obtiene el rol del usuario logueado.
     *
     * @param request Instancia de HttpServletRequest
     * @return El rol del usuario logueado
     */
    public static String getLoginUserRol(HttpServletRequest request)
    {
        // Obtiene el UserDTO del usuario logueado
        UserDTO userDTO = getUserLogin(request);

        // Si el usuario no es nulo
        if (userDTO != null) {
            // Devuelve el rol del usuario logueado
            return userDTO.getRol();
        }

        // Devuelve No aplicable si no hay usuario logueado
        return "n/a";
    }

    /**
     * Obtiene la imagen en formato Base64 del usuario logueado.
     *
     * @param request Instancia de HttpServletRequest
     * @return una cadena con la imagen en formato Base64 del usuario si está disponible,
     *         "App/img/defaultUser.svg" si la imagen está en blanco,
     *         o "n/a" si no hay usuario logueado.
     */
    public static String getLoginImagen(HttpServletRequest request) {
        // Obtiene el UserDTO del usuario logueado
        UserDTO userDTO = getUserLogin(request);

        if (userDTO != null && userDTO.getImagen() != null) {
            return (userDTO.getImagen().isBlank()) ? "App/img/defaultUser.svg" : userDTO.getImagen();
        }

        // Devuelve No aplicable si no hay usuario logueado
        return "App/img/defaultUser.svg";
    }


    /**
     * Devuelve si el usuario está logueado, incluso si es administrador.
     *
     * @param server Instancia de ActionServer
     * @param isAdmin Indica si se quiere comprobar si es administrador o no
     * @return El rol del usuario logueado*
     */
    public static boolean isUserLogIn(ActionServer server, boolean isAdmin)
    {
        // Se desactiva la cache del navegador para esta página
        disableCacheWebBrowser(server);

        // Obtiene el UserDTO del usuario logueado
        UserDTO userDTO = getUserLogin(server.request());

        // Si el usuario no está logueado lo redirije a la página de login
        if (userDTO == null) {
            return false;
        }
        else
        {
            // Si se quiere comprobar si es administrador y el usuario no lo es, devuelve falso
            if(isAdmin && !userDTO.getRol().equalsIgnoreCase("admin"))
            {
                return false;
            }

            return true;
        }
    }
}