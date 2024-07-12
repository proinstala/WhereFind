package io.proinstala.wherefind.identidad.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.proinstala.wherefind.identidad.services.IdentidadService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;




@WebServlet(urlPatterns = IdentidadController.BASE_API_IDENTIDAD + "/*")
public class IdentidadController  extends BaseHttpServlet {

    protected static final String BASE_API_IDENTIDAD = "/api/identidad";

    private final IdentidadService identidadServicio = new IdentidadService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene el EndPoint que debe usar
        String endPoint = getAction(request, BASE_API_IDENTIDAD);
        System.out.println("EndPoint GET : " + endPoint);

        switch(endPoint){
            case "/logout":
                identidadServicio.logOut(request, response);
                break;
            default:
                System.out.println("Accion no permitida");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene el EndPoint que debe usar
        String endPoint = getAction(request, BASE_API_IDENTIDAD);
        System.out.println("EndPoint POST : " + endPoint);

        switch(endPoint){
            case "/login":
                identidadServicio.logIn(request, response);
                break;
            default:
                System.out.println("Accion no permitida");
        }
    }
}
