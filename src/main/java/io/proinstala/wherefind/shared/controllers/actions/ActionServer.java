package io.proinstala.wherefind.shared.controllers.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase ActionServer que encapsula una solicitud y respuesta HTTP.
 *
 * @param request  la solicitud HTTP.
 * @param response la respuesta HTTP.
 */
public record ActionServer(HttpServletRequest request, HttpServletResponse response) {

/**
     * Obtiene un parámetro de la solicitud HTTP.
     *
     * @param name       el nombre del parámetro que se desea obtener.
     * @param porDefecto el valor por defecto que se devuelve si el parámetro no existe.
     * @return el valor del parámetro si existe, o el valor por defecto si no.
     */
    public String getRequestParameter(String name, String porDefecto)
    {
        // Obtiene del request el map de parámetros
        Map<String, String[]> parametros = new HashMap<>(request.getParameterMap());

        // Si se está procesando una operación PUT
        if (request.getMethod().equals("PUT"))
        {
            // Intenta obtener los parámetros de dicha operación
            getParametrosPut(parametros);
        }

        // En caso de encontar el parámetros
        if (parametros.containsKey(name))
        {
            // Se devuelve la primera ocurrencia dentro del arreglo
            return parametros.get(name)[0];
        }

        // Se devuelve el valor por defecto
        return porDefecto;
    }

    /**
     * Obtiene y procesa los parámetros de una solicitud HTTP PUT con el tipo de contenido
     * 'application/x-www-form-urlencoded'. Los parámetros se extraen del cuerpo de la solicitud
     * y se configuran como atributos en el objeto de solicitud.
     *
     * <p>Este método lee el cuerpo de la solicitud PUT, lo divide en pares clave-valor,
     * y establece estos pares como atributos de la solicitud para su posterior procesamiento.
     *
     */
    public void getParametrosPut(Map<String, String[]> parametros)
    {
        // Leer el cuerpo de la solicitud PUT
        StringBuilder sb = new StringBuilder();
        String line;
        try
        {
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        catch (IOException e)
        {
            return;
        }

        // Convertir el cuerpo de la solicitud a una cadena
        String requestBody = sb.toString();

        // Parsear el cuerpo como parámetros x-www-form-urlencoded
        String[] parameters = requestBody.split("&");
        for (String param : parameters) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                parametros.put(pair[0], new String[]{pair[1]});
            }
        }
    }

}
