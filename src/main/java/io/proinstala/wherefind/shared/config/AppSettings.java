package io.proinstala.wherefind.shared.config;


import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import io.proinstala.wherefind.shared.dtos.EmailSettingsDTO;


public class AppSettings {

    private static String rutaBase;

    public static void setRutaBase(String rutaBase) {
        AppSettings.rutaBase = rutaBase;
    }

    private static EmailSettingsDTO emailSettingsDTO = null;

    public static EmailSettingsDTO getEmailSettings()
    {
        if (emailSettingsDTO == null)
        {
            Gson gson = new Gson();
            try
            {
                try (Reader reader = Files.newBufferedReader(Paths.get(rutaBase + "/App/web/config/emails.settings.json")))
                {
                    emailSettingsDTO = gson.fromJson(reader, EmailSettingsDTO.class);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                emailSettingsDTO = new EmailSettingsDTO();
            }
        }

        return emailSettingsDTO;
    }

}
