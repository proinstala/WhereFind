package io.proinstala.wherefind.shared.consts.urls;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.proinstala.wherefind.shared.consts.urls.enums.UrlApp;
import io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad;


public class UrlsInternas {

    private static ReentrantLock lock = new ReentrantLock();

    private static Map<UrlIdentidad, String> urlsIdentidad = null;
    private static Map<UrlApp, String> urlsApp = null;

    private static void createUrlsIdentidad()
    {
        urlsIdentidad = new EnumMap<>(UrlIdentidad.class);

        urlsIdentidad.put(UrlIdentidad.LOGIN, "account/login");
        urlsIdentidad.put(UrlIdentidad.REGISTRAR, "account/registrar");
        urlsIdentidad.put(UrlIdentidad.MODIFICAR, "account/modificarUsuario");
    }


    private static void createUrlsApp()
    {
        urlsApp = new EnumMap<>(UrlApp.class);

        urlsApp.put(UrlApp.HOME, "dashboard");
    }

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


    public static String getIdentidadUri(UrlIdentidad url)
    {
        inicializar();

        return urlsIdentidad.get(url);
    }

    public static String getAppdUri(UrlApp url)
    {
        inicializar();

        return urlsApp.get(url);
    }

}
