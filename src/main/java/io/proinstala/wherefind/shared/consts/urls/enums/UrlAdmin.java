package io.proinstala.wherefind.shared.consts.urls.enums;

public enum UrlAdmin {
    USER_LISTA("admin/users"),
    USER_EDIT("admin/users/edit");

    // Variables de instancia
    private final String uri;

    // Constructor
    UrlAdmin(String uri)
    {
        this.uri = uri;
    }

    // Métodos para acceder a las propiedades
    public String getUri()
    {
        return uri;
    }

}
