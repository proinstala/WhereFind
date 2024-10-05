<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link href="App/css/breadcrumbs.css" rel="stylesheet" type="text/css"/>

<div class="breadcrumb-container">

    <%!
        // Para mapear las url y saber como procesarlas
        Map<String, String> urlMapping = new HashMap<>();

        // Para mapear las url y saber como procesarlas
        public Map<String, String> urlMappingName = new HashMap<>();

        // Para determinar cual es la última parte de una url que debe ser añadida a las breadcrumbs
        public List<String> urlLastBreadCrumbName = new ArrayList<>();


        // Para devolver el texto real modificado y adaptado por parte de una uri
        String getTextoMigaFromUri(String porDefecto, String uri)
        {
            // Inicializa la variable
            String apparentTexto = null;

            for (Map.Entry<String, String> entry : urlMappingName.entrySet())
            {
                String key = entry.getKey();

                // Comprobamos si la URL termina con la clave
                if (uri.endsWith(key))
                {
                    apparentTexto = entry.getValue();
                    break;
                }
            }

            // Si no se encuentra el mapeo, usar el porDefecto real como respaldo
            if (apparentTexto == null)
            {
                apparentTexto = porDefecto;
            }

            return apparentTexto;
        }

        // Para devolver el texto real modificado y adaptado
        String getTextoMiga(String porDefecto, String uri)
        {
            // Obtener la URI aparente desde el mapa
            String apparentTexto = urlMappingName.get(porDefecto.toLowerCase());

            // Si no se encuentra el mapeo, usar el porDefecto real como respaldo
            if (apparentTexto == null)
            {
                apparentTexto = getTextoMigaFromUri(porDefecto, uri);
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
        urlMapping.put("/App/web/identidad/identidadUserEditar.jsp".toLowerCase(), "/admin/users/edit");



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
        urlMappingName.put("users/edit".toLowerCase(), "Modificar Usuario");

        // Dirección
        urlMappingName.put("direccion".toLowerCase(), "Dirección");
        urlMappingName.put("direcciones".toLowerCase(), "Direcciones");
        urlMappingName.put("direcciones/edit".toLowerCase(), "Editar Dirección");

        // Añadir las partes que deben ser consideradas como partes finales de las breadcrumbs
        urlLastBreadCrumbName.add("edit".toLowerCase());


        String[] uriParts = apparentURI.split("/");

        // Verifica si el último elemento es un número
        if (uriParts.length > 0 && uriParts[uriParts.length - 1].matches("\\d+")) {
            // Excluir el último elemento si es un número
            uriParts = Arrays.copyOf(uriParts, uriParts.length - 1);
        }

        // Recorrer uriParts desde el último elemento hacia el primero
        int indexToRemoveFrom = -1;

        // Buscar coincidencia exacta con los elementos en urlLastBreadCrumbName
        for (int i = uriParts.length - 1; i >= 0; i--) {
            String part = uriParts[i].toLowerCase();
            if (urlLastBreadCrumbName.contains(part)) {
                indexToRemoveFrom = i + 1;
                break;
            }
        }

        // Si encontramos una coincidencia, excluimos desde la coincidencia hasta el final
        if (indexToRemoveFrom != -1) {
            //String[] newUriParts = Arrays.copyOfRange(uriParts, 0, indexToRemoveFrom);
            uriParts = Arrays.copyOf(uriParts, indexToRemoveFrom);
        }
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

                    String link = "";
                    if (i != uriParts.length -1)
                    {
                        link = "href=\"" + path + "\"";
                    }
        %>
                        <li data-id="<%= uriParts[i] %>">
                            <a <%= link %>>
                                <span class="icon"> <i class="las"></i></span>
                                <span class="text"><%= getTextoMiga(uriParts[i], path) %></span>
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