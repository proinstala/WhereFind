package io.proinstala.wherefind.shared.consts.textos;
/**
 * Clase LocaleApp que contiene constantes para mensajes de error e información utilizados en la aplicación.
 */
public class LocaleApp {

    // Mensajes de error
    /** Mensaje genérico de error. */
    public static final String ERROR_SE_HA_PRODUCIDO_UN_ERROR = "Se ha producido un error.";

    /** Mensaje cuando un usuario no es encontrado. */
    public static final String ERROR_USUARIO_NO_ENCONTRADO = "Usuario no encontrado o los datos introducidos son incorrectos.";

    /** Mensaje para indicar que un parámetro no es correcto. */
    public static final String ERROR_PARAMETRO_NO_CORRECTO = "El parámetro no es correcto.";

    /** Mensaje cuando faltan parámetros necesarios. */
    public static final String ERROR_FALTAN_PARAMETROS = "Faltan parámetros para poder realizar la acción solicitada.";

    /** Mensaje para indicar que una acción no está permitida. */
    public static final String ERROR_ACCION_NO_PERMITIDA = "Acción no permitida.";

    // Mensajes de información
    /** Mensaje para indicar que un usuario ha sido creado correctamente. */
    public static final String INFO_CREATE_USER = "Se ha creado el usuario correctamente.";

    /** Mensaje para indicar que un usuario ha sido modificado correctamente. */
    public static final String INFO_UPDATE_USER = "Se ha modificado el usuario correctamente.";

    /** Mensaje para indicar que un usuario ha sido eliminado correctamente. */
    public static final String INFO_DELETE_USER = "Se ha eliminado el usuario correctamente.";

    /** Título del email que recibirá el usuario cuando solita un cambio de password. */
    public static final String EMAIL_CAMBIAR_PASSWORD_TITULO = "Cambio de Contraseña Solicitado";

    /** Cuerpo del email que recibirá el usuario cuando solita un cambio de password. */
    public static final String EMAIL_CAMBIAR_PASSWORD_CUERPO =    "Hola <b>%s</b>,\n\n<br/><br/>"
                                                                + "Recibimos tu solicitud para cambiar la contraseña. Para proceder, por favor haz clic en el siguiente enlace:\n\n<br/><br/>"
                                                                + "%s\n\n<br/><br/>"
                                                                + "Este enlace es válido por las próximas 24 horas. Si no solicitaste este cambio, ignora este correo.\n\n<br/><br/>"
                                                                + "Gracias,\n\n<br/><br/>"
                                                                + "<b>Equipo de Soporte</b>";

    /** Mensaje que se le muestra al usuario cuando solicita recuperar su password. */
    public static final String EMAIL_CAMBIAR_PASSWORD_INFO  = "Se le ha enviado un email con las instrucciones para recuperar su password.";
    
    
    
    /** Mensaje para indicar que los datos han sido modificados correctamente. */
    public static final String INFO_UPDATE_OK = "Se han modificado los datos correctamente.";

}