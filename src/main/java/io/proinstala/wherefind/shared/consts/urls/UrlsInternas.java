package io.proinstala.wherefind.shared.consts.urls;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.proinstala.wherefind.shared.consts.urls.enums.UrlApp;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad;

/**
 * Clase que proporciona las URLs internas de la aplicación, organizadas por categorías.
 * Utiliza mapas para almacenar URLs relacionadas con la identidad del usuario y las URLs
 * de la aplicación.
 *
 * La inicialización de estas URLs se realiza de manera segura para multihilos utilizando
 * un bloqueo (ReentrantLock).
 */
public class UrlsInternas {
    // Bloqueo para asegurar que la inicialización de las URLs sea segura en un entorno multihilos.
    private static ReentrantLock lock = new ReentrantLock();

    // Mapas para almacenar las URLs categorizadas.
    private static Map<UrlIdentidad, String> urlsIdentidad = null;
    private static Map<UrlApp, String> urlsApp = null;

    /**
     * Método privado que crea e inicializa el mapa de URLs relacionadas con la identidad del usuario.
     */
    private static void createUrlsIdentidad()
    {
        urlsIdentidad = new EnumMap<>(UrlIdentidad.class);

        urlsIdentidad.put(UrlIdentidad.LOGIN, "account/login");
        urlsIdentidad.put(UrlIdentidad.REGISTRAR, "account/registrar");
        urlsIdentidad.put(UrlIdentidad.MODIFICAR, "account/modificarUsuario");
    }

    /**
     * Método privado que crea e inicializa el mapa de URLs relacionadas con la aplicación.
     */
    private static void createUrlsApp()
    {
        urlsApp = new EnumMap<>(UrlApp.class);

        urlsApp.put(UrlApp.HOME, "dashboard");
    }

    /**
     * Método privado que inicializa los mapas de URLs si aún no han sido creados.
     * Utiliza un bloqueo para asegurar que la inicialización sea segura en un entorno multihilos.
     */
    private static void inicializar()
    {
        lock.lock();
        try
        {
            if (urlsIdentidad == null)
            {
                createUrlsIdentidad();
            }

            if (urlsApp == null)
            {
                createUrlsApp();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Obtiene la URL correspondiente a la identidad del usuario basada en la clave proporcionada.
     *
     * @param url La clave del tipo de URL de identidad a obtener.
     * @return La URL correspondiente a la clave proporcionada, o null si no se encuentra.
     */
    public static String getIdentidadUri(UrlIdentidad url)
    {
        inicializar();

        return urlsIdentidad.get(url);
    }

    /**
     * Obtiene la URL correspondiente a la aplicación basada en la clave proporcionada.
     *
     * @param url La clave del tipo de URL de aplicación a obtener.
     * @return La URL correspondiente a la clave proporcionada, o null si no se encuentra.
     */
    public static String getAppdUri(UrlApp url)
    {
        inicializar();

        return urlsApp.get(url);
    }

}
