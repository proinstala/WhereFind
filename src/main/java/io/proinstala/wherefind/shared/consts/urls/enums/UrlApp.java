package io.proinstala.wherefind.shared.consts.urls.enums;

public enum UrlApp {
    HOME("dashboard");

    // Variables de instancia
    private final String uri;

    // Constructor
    UrlApp(String uri)
    {
        this.uri = uri;
    }

    // Métodos para acceder a las propiedades
    public String getUri()
    {
        return uri;
    }

}
