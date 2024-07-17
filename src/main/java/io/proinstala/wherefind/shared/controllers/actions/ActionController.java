package io.proinstala.wherefind.shared.controllers.actions;

/**
 * Clase ActionController que representa un controlador de acciones con varios parámetros.
 *
 * @param fullUri     la URI completa de la acción.
 * @param uri         la URI parcial de la acción.
 * @param actionType  el tipo de acción.
 * @param parametros  los parámetros de la acción.
 * @param server      el servidor de acciones asociado.
 */
public record ActionController(String fullUri, String uri, Object actionType, String[] parametros, ActionServer server) {

    /**
     * Obtiene un valor entero de los parámetros en una posición específica.
     *
     * @param index el índice del parámetro en el arreglo.
     * @return el valor entero del parámetro en la posición especificada, o -1 si ocurre una excepción de formato.
     */
    public int getIntFromParametros(int index)
    {
        int id;
        try
        {
            id = Integer.parseInt(parametros[index]);
        }
        catch (NumberFormatException e)
        {
            id = -1;
        }

        return id;
    }

}
