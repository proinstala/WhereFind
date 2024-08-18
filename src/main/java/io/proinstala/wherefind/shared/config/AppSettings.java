package io.proinstala.wherefind.shared.config;


import java.io.File;
import java.io.FileReader;
import com.google.gson.Gson;
import io.proinstala.wherefind.shared.dtos.EmailSettingsDTO;


/**
 * Clase que almacena configuración para la app.
 */
public class AppSettings {

    private static final String APP_NAME = "WhereFind";

    /**
     * Devuelve la instancia de EmailSettingsDTO, o crea una nueva si no ha sido inicializada previamente.
     *
     * @return La instancia de EmailSettingsDTO.
     */
    public static EmailSettingsDTO getEmailSettings()
    {
        Gson gson = new Gson();
        try
        {
            // Lee el archivo de configuración de correos electrónicos.
            try (FileReader fileReader = new FileReader(getConfiguracionFullRuta("config/emails.settings.json")))
            {
                return gson.fromJson(fileReader, EmailSettingsDTO.class);
            }
        }
        catch (Exception e)
        {
            // Maneja la excepción y devuelve una nueva instancia de EmailSettingsDTO en caso de error.
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Obtiene la ruta completa de una configuración a partir de una ruta relativa proporcionada.
     * Además, crea el directorio correspondiente si no existe previamente.
     *
     * @param ruta La cadena que representa la ruta al archivo dentro de la aplicación
     *
     * @return Cadena con la ruta completa o null en caso de error
     */
    public static String getConfiguracionFullRuta(String ruta)
    {
        // Obtenemos directorio principal del usuario actual (~/)
        String userHome = System.getProperty("user.home");

        // Construimos la ruta completa añadiendo el nombre de la app y la ruta proporcionada
        String rutaFull = userHome + "/" + APP_NAME + "/" + ruta;

        // Reemplazamos los caracteres '\\' por '/' para manejar las plataformas Windows
        rutaFull = rutaFull.replace("\\", "/");

        // Creamos directorio si aún no existe
        createDirectoryFromPathFile(rutaFull);

        // Devolvemos la cadena con la ruta completa
        return rutaFull;
    }


    /**
     * Crea un directorio a partir de una ruta proporcionada.
     * Esto creará todos los subdirectorios intermedios que no existan previamente,
     * siempre y cuando se proporcione la ruta completa del archivo requerido para creación.
     *
     * @param rutaArchivo La cadena que representa la ruta hasta el archivo o directorio a crear
     *
     * @return Verdadero si el directorio fue creado correctamente o ya existía; Falso en caso contrario
     */
    public static boolean createDirectoryFromPathFile(String rutaArchivo)
    {
        try
        {
            // Crear un objeto File con la ruta del directorio
            File archivo = new File(rutaArchivo);

            // Obtiene el directorio padre (sin el nombre del archivo)
            File directorio = archivo.getParentFile();

            // Verificar si el directorio existe
            if (directorio != null && !directorio.exists())
            {
                // Crear el directorio
                return directorio.mkdirs(); // Crea los directorios necesarios
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
