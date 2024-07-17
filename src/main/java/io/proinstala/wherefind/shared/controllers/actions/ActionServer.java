package io.proinstala.wherefind.shared.controllers.actions;

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
        Map<String, String[]> parametros = request.getParameterMap();

        // En caso de encontar el parámetros
        if (parametros.containsKey(name))
        {
            // Se devuelve la primera ocurrencia dentro del arreglo
            return parametros.get(name)[0];
        }

        // Se devuelve el valor por defecto
        return porDefecto;
    }


}
