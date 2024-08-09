package io.proinstala.wherefind.shared.consts.urls;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad;


public class UrlsInternas {

    private static ReentrantLock lock = new ReentrantLock();
    private static Map<UrlIdentidad, String> urlsIdentidad = null;



    private static void createUrlsIdentidad()
    {
        urlsIdentidad = new EnumMap<>(UrlIdentidad.class);

        urlsIdentidad.put(UrlIdentidad.LOGIN, "account/login");
        urlsIdentidad.put(UrlIdentidad.REGISTRAR, "account/registrar");
        urlsIdentidad.put(UrlIdentidad.MODIFICAR, "modificarUsuario");
    }

    public static String getIdentidadUri(UrlIdentidad url)
    {
        lock.lock();
        try
        {
            if (urlsIdentidad == null)
            {
                createUrlsIdentidad();
            }
        }
        finally
        {
            lock.unlock();
        }
        return urlsIdentidad.get(url);
    }

}
