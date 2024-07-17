package io.proinstala.wherefind.shared.services;

import java.io.IOException;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase BaseService que proporciona métodos para gestionar respuestas JSON en servicios HTTP.
 */
public class BaseService {

    /**
     * Envía una respuesta JSON con los datos especificados.
     *
     * @param response la respuesta HTTP a la que se enviará el contenido.
     * @param data     los datos en formato JSON que se enviarán.
     * @return true si la respuesta se envió correctamente, false en caso contrario.
     */
    protected boolean responseJson(HttpServletResponse response, String data)
    {
        response.setContentType("application/json");
        try
        {
            response.getWriter().write(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Envía una respuesta JSON a partir de un objeto.
     *
     * @param response la respuesta HTTP a la que se enviará el contenido.
     * @param data     el objeto que se convertirá a JSON y se enviará.
     * @return true si la respuesta se envió correctamente, false en caso contrario.
     */
    protected boolean responseJson(HttpServletResponse response, Object data)
    {
        return responseJson(response, new GsonBuilder().create().toJson(data));
    }
}
