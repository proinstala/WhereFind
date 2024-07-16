package io.proinstala.wherefind.identidad;

import java.io.IOException;

import io.proinstala.wherefind.infraestructure.data.GestionPersistencia;
import io.proinstala.wherefind.infraestructure.data.interfaces.IUserService;
import io.proinstala.wherefind.shared.controllers.actions.ActionServer;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserSession {

    public static UserDTO login(String userName, String password, ActionServer server)
    {
        // Conecta con el Gestor de Permanencia
        IUserService gestor = GestionPersistencia.getUserService();

        // Obtiene los datos del usuario
        UserDTO userActual = gestor.getUser(userName, password);

        if (userActual != null)
        {
            HttpSession session = server.request().getSession();
            session.setAttribute("user", userActual);
        }

        return userActual;
    }

    public static void logOut(ActionServer server)
    {
        HttpSession session = server.request().getSession(false);
        if (session != null)
        {
            session.removeAttribute("user");
            session.invalidate();
        }

        redireccionar(server.response(), server.request().getContextPath()+"/index.jsp");
    }

    public static void redireccionar(ActionServer server, String uri, String uriNotLogin)
    {
        String finalUri = (getUserLogin(server.request()) == null) ? uriNotLogin : uri;
        redireccionar(server.response(), finalUri);
    }

    public static void redireccionar(HttpServletResponse response, String uri)
    {
        try
        {
            response.sendRedirect(uri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static UserDTO getUserLogin(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        return (UserDTO)session.getAttribute("user");
    }

    public static void redireccionarIsUserNotLogIn(ActionServer server)
    {
        // Se desactiva la cache del navegador para esta página
        disableCacheWebBrowser(server);

        UserDTO userActual = getUserLogin(server.request());
        if (userActual == null) {
            redireccionar(server.response(), server.request().getContextPath()+"/login.jsp");
        }
    }

    public static void disableCacheWebBrowser(ActionServer server)
    {
        server.response().setHeader("Cache-Control", "no-cache");
        server.response().setHeader("Cache-Control", "no-store");
        server.response().setHeader("Pragma", "no-cache");
        server.response().setDateHeader("Expires", -1);
    }


    public static String getLoginUserFullName(HttpServletRequest request)
    {
        UserDTO userActual = getUserLogin(request);
        if (userActual != null) {
            return userActual.getNombre() + " " + userActual.getApellidos();
        }

        return "n/a";
    }

    public static String getLoginUserRol(HttpServletRequest request)
    {
        UserDTO userActual = getUserLogin(request);
        if (userActual != null) {
            return userActual.getRol();
        }

        return "n/a";
    }


}