package io.proinstala.wherefind.identidad.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.proinstala.wherefind.identidad.services.IdentidadService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = IdentidadController.BASE_API_IDENTIDAD + "/*")
public class IdentidadController  extends BaseHttpServlet {

    protected static final String BASE_API_IDENTIDAD = "/api/identidad";

    private final IdentidadService identidadServicio = new IdentidadService();

    enum ActionType {
        ERROR,
        LOGOUT,
        LOGIN,
        USER,
        USERS,
    }

    @Override
    protected String getBaseApi()
    {
        return BASE_API_IDENTIDAD;
    }

    @Override
    protected Object getActionType(String action)
    {
        if (action != "") {
            action = action.toUpperCase();
            for (ActionType accion : ActionType.values()) {
                if (action.equals(accion.name())) {
                    return accion;
                }
            }
        }

        return ActionType.ERROR;
    }

    // EndPoint - GET :  /api/identidad/logout
    protected void apiLogOut(ActionController actionController)
    {
        identidadServicio.logOut(actionController.server());
    }

    // EndPoint - POST : /api/identidad/login
    protected void apiLogIn(ActionController actionController)
    {
        identidadServicio.logIn(actionController.server());
    }

    // EndPoint - GET : /api/identidad/user/{id}
    protected void apiGetUser(ActionController actionController)
    {
        identidadServicio.getUser(actionController);
    }

    // EndPoint - GET : /api/identidad/users
    protected void apiGetUsers(ActionController actionController)
    {
        identidadServicio.getUsers(actionController);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);

        switch(actionController.actionType()){
            case ActionType.LOGOUT :
                apiLogOut(actionController);
                break;

            case ActionType.USER:
                apiGetUser(actionController);
                break;

            case ActionType.USERS:
                apiGetUsers(actionController);
                break;

            default:
                System.out.println("Accion no permitida");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint POST : " + actionController.parametros()[0]);

        switch(actionController.actionType()){
            case ActionType.LOGIN :
                apiLogIn(actionController);
                break;
            default:
                System.out.println("Accion no permitida");
        }
    }
}
