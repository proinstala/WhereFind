package io.proinstala.wherefind.poblacion.controller;

import java.io.IOException;

import io.proinstala.wherefind.identidad.services.IdentidadService;
import io.proinstala.wherefind.poblacion.service.PoblacionService;
import io.proinstala.wherefind.shared.controllers.BaseHttpServlet;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = PoblacionController.BASE_API_POBLACION + "/*")
public class PoblacionController  extends BaseHttpServlet {
    protected static final String BASE_API_POBLACION = "/api/poblacion";

    private final PoblacionService poblacionServicio = new PoblacionService();


    /**
     * Tipos de acción que este controlador puede manejar.
     */
    enum ActionType {
        ERROR,
        POBLACIONES
    }

    @Override
    protected String getBaseApi()
    {
        return BASE_API_POBLACION;
    }

    @Override
    protected Object getActionType(String action)
    {
        // Si la acción no es nula
        if (action != "") {
            // Convierte el texto en mayúsculas
            action = action.toUpperCase();

            // Recorre todos los ActionType
            for (ActionType accion : ActionType.values()) {

                // Conprueba que action esté entre los ActionType
                if (action.equals(accion.name())) {
                    // Devuelve el ActionType encontrado
                    return accion;
                }
            }
        }

        // Devuelve el ActionType de error por no encontrar un ActionType coincidente
        return ActionType.ERROR;
    }

    protected void apiGetPoblaciones(ActionController actionController)
    {
        // Se llama al servicio para procese la acción requerida
        poblacionServicio.getPoblaciones(actionController);
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la información de la petición a la API
        ActionController actionController = getActionController(request, response);

        // Imprime en la salida del servidor el EndPoint
        System.out.println("EndPoint GET : " + actionController.parametros()[0]);

        // Dependiendo del ActionType, realizará una acción
        switch(actionController.actionType()){

            case ActionType.POBLACIONES:
                apiGetPoblaciones(actionController);
                break;

            default:
                responseError404(actionController.server().response(), "");
        }
    }


}
