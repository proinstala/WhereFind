package io.proinstala.wherefind.Identity;

import java.io.IOException;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserSession {

    public static UserDTO Login(String userName, String password, HttpServletRequest request)
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

    public static void LogOut(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.removeAttribute("user");
        }

        Redireccionar(response, "index.jsp");
    }

    public static void Redireccionar(HttpServletRequest request, HttpServletResponse response, String uri, String uriNotLogin)
    {
        String finalUri = (GetUserLogin(request) == null) ? uriNotLogin : uri;
        Redireccionar(response, finalUri);
    }

    public static void Redireccionar(HttpServletResponse response, String uri)
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

    public static UserDTO GetUserLogin(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        return (UserDTO)session.getAttribute("user");
    }

    public static void RedireccionarIsUserNotLogIn(HttpServletRequest request, HttpServletResponse response)
    {
        UserDTO userActual = GetUserLogin(request);
        if (userActual == null) {
            Redireccionar(response, request.getContextPath()+"/login.jsp");
        }
    }
}