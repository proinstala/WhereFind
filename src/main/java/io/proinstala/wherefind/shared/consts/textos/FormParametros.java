package io.proinstala.wherefind.shared.consts.textos;

/**
 * Clase ConstParametros que contiene constantes para los parámetros utilizados en la gestión de usuarios.
 */
public class FormParametros {

    /** Nombre de usuario. */
    public static final String PARAM_USUARIO_USERNAME = "nombreUsuario";

    /** Contraseña del usuario. */
    public static final String PARAM_USUARIO_PASSWORD = "passwordUsuario";

    /** Nueva Contraseña del usuario*/
    public static final String PARAM_USUARIO_NUEVO_PASSWORD = "nuevoPassword";
    
    /** Nueva Contraseña del usuario*/
    public static final String PARAM_USUARIO_CONFIRMACION_PASSWORD = "confirmPassword";

    /** Rol del usuario. */
    public static final String PARAM_USUARIO_ROL = "rolUsuario";

    /** Nombre real del usuario. */
    public static final String PARAM_USUARIO_NOMBRE = "nombreRealUsuario";

    /** Apellidos del usuario. */
    public static final String PARAM_USUARIO_APELLIDOS = "apellidoRealUsuario";

    /** Correo electrónico del usuario. */
    public static final String PARAM_USUARIO_EMAIL = "emailUsuario";

    /** Imagen del usuario. */
    public static final String PARAM_USUARIO_IMAGEN = "imagenUsuarioB64";
    
    
    
    //Direccion ----------------------------------------------------------------
    
    public static final String PARAM_DIRECCION_ID = "direccion";
    
    /** Nombre de la calle de la dirección. */
    public static final String PARAM_DIRECCION_CALLE = "calle";
    
    /** Número de la dirección. */
    public static final String PARAM_DIRECCION_NUMERO = "numero";
    
    /** Códgigo postal de la dirección. */
    public static final String PARAM_DIRECCION_CODIGO_POSTAL = "codigoPostal";
    
    /** Localidad de la dirección. */
    public static final String PARAM_DIRECCION_LOCALIDAD = "localidad";
    
    /** Provincia de la dirección. */
    public static final String PARAM_DIRECCION_PROVINCIA = "provincia";
    
    
    //Provincia ----------------------------------------------------------------
    
    /** Nombre de la provincia. */
    public static final String PARAM_PROVINCIA_NOMBRE = "nombre";
    
    
    //Localidad ----------------------------------------------------------------
    
    /** Nombre de la localidad. */
    public static final String PARAM_LOCALIDAD_NOMBRE = "nombre";
    
    /** Provincia de la localidad. */
    public static final String PARAM_LOCALIDAD_PROVINCIA = "provincia";
    
    
    //Contacto -----------------------------------------------------------------
    public static final String PARAM_CONTACTO_NOMBRE = "nombre";
    public static final String PARAM_CONTACTO_APELLIDO = "apellido";
    public static final String PARAM_CONTACTO_PUESTO = "puesto";
    public static final String PARAM_CONTACTO_TELEFONO = "telefono";
    public static final String PARAM_CONTACTO_EMAIL = "email";
    public static final String PARAM_CONTACTO_PROVEEDOR = "proveedor";
    
    //Puesto Trabajo -----------------------------------------------------------
    public static final String PARAM_PUESTO_TRABAJO_NOMBRE = "nombre";
    
    //Proveedor ----------------------------------------------------------------
    public static final String PARAM_PROVEEDOR_NOMBRE = "nombre";
    public static final String PARAM_PROVEEDOR_DESCRIPCION = "descripcion";
    public static final String PARAM_PROVEEDOR_PAGINA_WEB = "paginaWeb";
    public static final String PARAM_PROVEEDOR_IMAGEN = "imagenProveedorB64";
    public static final String PARAM_PROVEEDOR_DIRECCION = "direccion";
}
