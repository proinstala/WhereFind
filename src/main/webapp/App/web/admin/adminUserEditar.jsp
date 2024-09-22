<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlAdmin"%>
<%
    // Se declara e instancia un ActionServer
    ActionServer actionServer = new ActionServer(request, response);

    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(actionServer)){
        // Detiene la ejecución de este servlet
        return;
    }

    // Si el usuario está logueado pero no es administrado
    if (!UserSession.isUserLogIn(actionServer, true))
    {
        // Obtiene un error 403
        BaseHttpServlet.responseError403(actionServer.response(), "");
        return;
    }

    // Se obtiene el ActionController para determinar la acción o procesar algún parámetro
    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "admin/users/edit");

    // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
    int id = actionController.getIntFromParametros(1);

    if (id == -1)
    {
        // Obtiene un error 403
        BaseHttpServlet.responseError403(actionController.server().response(), "");
        return;
    }


    // Se establece un atributo en la solicitud actual con los datos del usuario recuperado que serán accesibles para la vista correspondiente
    request.setAttribute("userIdByAdmin", id);
    request.setAttribute("isEditarByAdmin", true);
%>

<%@ include file="/App/web/identidad/identidadUserEditar.jsp" %>

