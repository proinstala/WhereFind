package io.proinstala.wherefind.shared.consts.urls.enums;

public enum UrlIdentidad {
    LOGIN("account/login"),
    REGISTRAR("account/registrar"),
    MODIFICAR("account/modificar"),
    RECOVERY("account/recovery");


    // Variables de instancia
    private final String uri;

    // Constructor
    UrlIdentidad(String uri)
    {
        this.uri = uri;
    }

    // Métodos para acceder a las propiedades
    public String getUri()
    {
        return uri;
    }
}
