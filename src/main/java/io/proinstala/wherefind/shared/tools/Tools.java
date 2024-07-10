package io.proinstala.wherefind.shared.tools;

import io.proinstala.wherefind.shared.dto.UserDto;

public class Tools {

    public static String getMensajeResultado(UserDto usuario)
    {
        if(usuario==null)  {
           return "<b>No se ha encontrado el usuario o los datos introducidos son incorrectos</b> <br/>";
        } else {
           return "<b>"+usuario.getUserName()+" : </b> <span>"+usuario.getPassword()+"</span> <span>"+usuario.getRol()+"</span> <br/>";
        }
    }

}
