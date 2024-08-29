<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="breadcrumb-container">

    <style>
        .breadcrumb-container {
            height: 1.7em;
            margin-bottom: 10px;
        }

        .breadcrumb {
            margin-left : 1.5em;
            list-style: none;
            padding: 0;
        }
        .breadcrumb li {
            display: inline;
            margin-right: 5px;
            font-size: 1.2em;
        }
        .breadcrumb li::after {
            content: ">";
            margin-left: 5px;
        }
        .breadcrumb li:last-child::after {
            content: "";
        }


        .breadcrumb li.active {
            font-weight: 600;
        }

        .breadcrumb li,
        .breadcrumb li a  {
            color: rgba(255,255,255, 0.95);
        }

        .breadcrumb li a  {
            text-decoration: none;
        }

        .breadcrumb li::before {
            content: "\f015";
            margin-right: 5px;
            font-size: 24px;
            display: inline-block;
            vertical-align: middle;
        }

        .breadcrumb li.active::before {

        }

        .breadcrumb li[data-id="inicio"]::before {
            content: "";
        }

        .breadcrumb li[data-id="inicio"] a {
            padding: 10px;
            text-decoration: none;
        }

        .breadcrumb li[data-id="inicio"] a i {
            cursor: pointer;
            font-size: 24px;
            margin-left: -20px;
            margin-top: 1px;
            position: absolute;
        }


        .breadcrumb li[data-id="account"]::before {
            content: "\f007";
        }

        .breadcrumb li[data-id="modificar"]::before {
            content: "\f4ff";
        }


        .breadcrumb li[data-id="admin"]::before {
            content: "\f505";
        }

        .breadcrumb li[data-id="users"]::before {
            content: "\f0c0";
        }

        .breadcrumb li[data-id="edit"]::before {
            content: "\f4ff";
        }


        .breadcrumb li[data-id="direccion"]::before {
            content: "\f041";
        }

        .breadcrumb li[data-id="adminDireccion"]::before {
            content: "\f2b9";
        }


    </style>

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
        <li data-id="inicio"><a href="<%= uriHome %>"><i class="las la-home"></i></a></li>
        <%
            String path = uriHome;
            for (int i = 1; i < uriParts.length; i++)
            {
                if (!uriParts[i].isEmpty())
                {
                    path += "/" + uriParts[i];
                    if (i == uriParts.length - 1)
                    {
        %>
                        <li class="las active" data-id="<%= uriParts[i] %>"><%= getTextoMiga(uriParts[i]) %></li>
        <%
                    }
                    else
                    {
        %>
                        <li class="las" data-id="<%= uriParts[i] %>"><a href="<%= path %>"><%= getTextoMiga(uriParts[i]) %></a></li>
        <%
                    }
                }
            }
        %>
    </ul>
</div>