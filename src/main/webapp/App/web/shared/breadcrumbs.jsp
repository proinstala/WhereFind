<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link href="App/css/breadcrumbs.css" rel="stylesheet" type="text/css"/>

<div class="breadcrumb-container">

    <%!
        // Para mapear las url y saber como procesarlas
        java.util.Map<String, String> urlMapping = new java.util.HashMap<>();

        // Para mapear las url y saber como procesarlas
        public java.util.Map<String, String> urlMappingName = new java.util.HashMap<>();

        // Para devolver el texto real modificado y adaptado
        String getTextoMiga(String porDefecto)
        {
            // Obtener la URI aparente desde el mapa
            String apparentTexto = urlMappingName.get(porDefecto.toLowerCase());

            // Si no se encuentra el mapeo, usar el porDefecto real como respaldo
            if (apparentTexto == null)
            {
                apparentTexto = porDefecto;
            }

            return apparentTexto;
        }

        String getUrlMiga(String[] partes, int index, String uriHome)
        {
            String path = uriHome;
            for (int i = 1; i <= index; i++)
            {
                if (!partes[i].isEmpty())
                {
                    path += "/" + partes[i];
                }
            }

            return path;
        }
    %>



    <%
        String uriApp   = request.getContextPath();
        String uriHome  = uriApp;
        String uriFull = request.getRequestURI();
        String uri     = uriFull.replace(uriApp, "");


        // Añadir mapeos de URI real -> URI aparente
        urlMapping.put("/App/web/identidad/modificarUsuario.jsp".toLowerCase(), "/admin/users/edit");



        // Obtener la URI aparente desde el mapa
        String apparentURI = urlMapping.get(uri.toLowerCase());

        // Si no se encuentra el mapeo, usar la URI real como respaldo
        if (apparentURI == null)
        {
            apparentURI = uri;
        }


        // Añadir mapeos de nombre real -> nombre Nuevo
        // account
        urlMappingName.put("account".toLowerCase(), "Cuenta");
        urlMappingName.put("modificar".toLowerCase(), "Editar");


        // Admin
        urlMappingName.put("admin".toLowerCase(), "Administrar");
        urlMappingName.put("users".toLowerCase(), "Usuarios");
        urlMappingName.put("edit".toLowerCase(), "Editar");

        // Dirección
        urlMappingName.put("direccion".toLowerCase(), "Dirección");
        urlMappingName.put("adminDireccion".toLowerCase(), "Direcciones");

        String[] uriParts = apparentURI.split("/");
    %>



<%-- <%= uriApp %><br>
<%= uriFull %><br>
<%= uri %><br> --%>

    <ul class="breadcrumb">
        <%
            String path = uriHome;
            //for (int i = 1; i < uriParts.length; i++)
            for (int i = uriParts.length - 1; i >= 1; i--)
            {
                if (!uriParts[i].isEmpty())
                {
                    if (uriParts[i].equals("dashboard"))
                    {
                        continue;
                    }

                    path = getUrlMiga(uriParts, i, uriHome);
        %>
                        <li data-id="<%= uriParts[i] %>">
                            <a href="<%= path %>">
                                <span class="icon las"></span>
                                <span class="text"><%= getTextoMiga(uriParts[i]) %></span>
                            </a>
                        </li>
        <%
                }
            }
        %>


        <li>
            <a href="<%= uriHome %>">
                <span class="icon las la-home"></span>
                <span class="text"></span>
            </a>
        </li>

    </ul>
</div>