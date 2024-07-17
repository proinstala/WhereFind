package io.proinstala.wherefind.shared.services;

import java.io.IOException;
import com.google.gson.GsonBuilder;

import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.dtos.UserDTO;
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

    /**
     * Crea un objeto ResponseDTO con los parámetros especificados.
     *
     * @param isError Indicador de error (1 si es un error, 0 si no lo es)
     * @param isUrl   Indicador de URL (1 si la respuesta contiene una URL, 0 si no lo hace)
     * @param mensaje Mensaje de respuesta
     * @param objecto Información relacionada con la respuesta
     * @return El objeto ResponseDTO creado
     */
    protected ResponseDTO createResponseDTO(int isError, int isUrl, String mensaje, Object objecto)
    {
        return new ResponseDTO(isError, isUrl, mensaje, objecto);
    }

    /**
     * Devuelve un objeto ResponseDTO con error y mensaje especificados.
     *
     * @param mensaje Mensaje de respuesta
     * @return El objeto ResponseDTO con error
     */
    protected ResponseDTO getResponseError(String mensaje)
    {
        return getResponseError(mensaje, new UserDTO());
    }

    /**
     * Devuelve un objeto ResponseDTO con error, mensaje y objecto especificados.
     *
     * @param mensaje Mensaje de respuesta
     * @param objecto Información relacionada con la respuesta
     * @return El objeto ResponseDTO con error
     */
    protected ResponseDTO getResponseError(String mensaje, Object objecto)
    {
        return createResponseDTO(1, 0, mensaje, objecto);
    }

    /**
     * Devuelve un objeto ResponseDTO con resultado OK y los parámetros especificados.
     *
     * @param mensaje Mensaje de respuesta
     * @param objecto Información relacionada con la respuesta
     * @param isUrl   Indicador de URL (1 si la respuesta contiene una URL, 0 si no lo hace)
     * @return El objeto ResponseDTO con resultado OK
     */
    protected ResponseDTO getResponseOk(String mensaje, Object objecto, int isUrl)
    {
        return createResponseDTO(0, isUrl, mensaje, objecto);
    }


}
