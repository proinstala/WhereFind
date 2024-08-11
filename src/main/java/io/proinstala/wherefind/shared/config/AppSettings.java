package io.proinstala.wherefind.shared.config;


import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import io.proinstala.wherefind.shared.dtos.EmailSettingsDTO;


/**
 * Clase que almacena configuración para la app.
 */
public class AppSettings {

    /**
     * Ruta base donde se encuentran los archivos de configuración.
     */
    private static String rutaBase;

    /**
     * Establece la ruta base.
     *
     * @param rutaBase La nueva ruta base.
     */
    public static void setRutaBase(String rutaBase) {
        AppSettings.rutaBase = rutaBase;
    }

    /**
     * Instancia de EmailSettingsDTO que se devuelve en caso de no haber sido inicializada previamente.
     */
    private static EmailSettingsDTO emailSettingsDTO = null;

    /**
     * Devuelve la instancia de EmailSettingsDTO, o crea una nueva si no ha sido inicializada previamente.
     *
     * @return La instancia de EmailSettingsDTO.
     */
    public static EmailSettingsDTO getEmailSettings()
    {
        if (emailSettingsDTO == null)
        {
            Gson gson = new Gson();
            try
            {
                // Lee el archivo de configuración de correos electrónicos.
                try (Reader reader = Files.newBufferedReader(Paths.get(rutaBase + "/App/web/config/emails.settings.json")))
                {
                    emailSettingsDTO = gson.fromJson(reader, EmailSettingsDTO.class);
                }
            }
            catch (Exception e)
            {
                // Maneja la excepción y devuelve una nueva instancia de EmailSettingsDTO en caso de error.
                e.printStackTrace();
                emailSettingsDTO = new EmailSettingsDTO();
            }
        }

        return emailSettingsDTO;
    }

}
