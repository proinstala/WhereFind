package io.proinstala.wherefind.identidad;

import java.io.IOException;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserSession {

    public static UserDTO login(String userName, String password, HttpServletRequest request)
    {
        // TODO: Conectar con mysql
        UserDTO userActual = new UserDTO(userName, password, "Admin");

        if (userActual != null)
        {
            HttpSession session = request.getSession();
            session.setAttribute("user", userActual);
        }

        return userActual;
    }

    public static void logOut(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.removeAttribute("user");
        }

        redireccionar(response, "index.jsp");
    }

    public static void redireccionar(HttpServletRequest request, HttpServletResponse response, String uri, String uriNotLogin)
    {
        String finalUri = (getUserLogin(request) == null) ? uriNotLogin : uri;
        redireccionar(response, finalUri);
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

    public static void redireccionarIsUserNotLogIn(HttpServletRequest request, HttpServletResponse response)
    {
        UserDTO userActual = getUserLogin(request);
        if (userActual == null) {
            redireccionar(response, request.getContextPath()+"/login.jsp");
        }
    }
}