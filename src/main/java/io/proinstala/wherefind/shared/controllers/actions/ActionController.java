package io.proinstala.wherefind.shared.controllers.actions;

public record ActionController(String fullUri, String uri, Object actionType, String[] parametros, ActionServer server) {

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
