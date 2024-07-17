package io.proinstala.wherefind.shared.tools;

import io.proinstala.wherefind.shared.dtos.UserDTO;

/**
 * Clase Tools que proporciona métodos utilitarios para el manejo de usuarios y otras operaciones generales.
 */
public class Tools {

    /**
     * Obtiene un mensaje de resultado basado en el estado de un usuario.
     *
     * @param usuario el objeto UserDTO que representa al usuario.
     * @return un mensaje formateado que indica si el usuario fue encontrado o no, junto con sus detalles.
     */
    public static String getMensajeResultado(UserDTO usuario)
    {
        if(usuario==null)  {
            return "<b>No se ha encontrado el usuario o los datos introducidos son incorrectos</b> <br/>";
        } else {
            return "<b>"+usuario.getUserName()+" : </b> <span>"+usuario.getPassword()+"</span> <span>"+usuario.getRol()+"</span> <br/>";
        }
    }


    /**
     * Pausa la ejecución del hilo actual durante un período especificado.
     *
     * @param ms el tiempo de espera en milisegundos.
     */
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
