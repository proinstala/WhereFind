package io.proinstala.wherefind.Identity;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.proinstala.wherefind.shared.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/login")
public class LogInServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreUsuario = request.getParameter("nombreUsuario");
        String passwordUsuario = request.getParameter("passwordUsuario");

        UserDto usuario = UserSession.Login(nombreUsuario, passwordUsuario, request);

        if (usuario != null)
        {
            UserSession.Redireccionar(request, response, "App/web/inicio/inicio.jsp", "login.jsp");
        }
    }
}