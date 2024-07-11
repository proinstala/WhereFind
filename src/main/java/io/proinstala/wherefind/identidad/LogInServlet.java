package io.proinstala.wherefind.identidad;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.proinstala.wherefind.shared.dtos.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/login")
public class LogInServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreUsuario = request.getParameter("nombreUsuario");
        String passwordUsuario = request.getParameter("passwordUsuario");

        UserDTO usuario = UserSession.login(nombreUsuario, passwordUsuario, request);

        if (usuario != null)
        {
            UserSession.redireccionar(request, response, "index.jsp", "login.jsp");
        }
        else
        {
            UserSession.redireccionar(response, "login.jsp");
        }
    }
}