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
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Configuración" />
</jsp:include>


<link href="App/css/general.css" rel="stylesheet" type="text/css"/>
<link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>
<link href="App/css/cards.css" rel="stylesheet" type="text/css"/>


<div class="contenedor__general">
    <div class="contenedor">
        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-120" id="form_busqueda">

                <div class="contenedor__formulario--cabecera conBotones margin-bottom-5">
                    <div>
                        <h1>Administrar</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>


                <div class="cards">
                    <jsp:include page="/App/web/shared/card.jsp" >
                        <jsp:param name="iconCard" value="las la-map-marked-alt" />
                        <jsp:param name="titleCard" value="Direcciones" />
                        <jsp:param name="descriptionCard" value="Gestione todas las direcciones, provincias y localidades." />
                        <jsp:param name="urlCard" value="#" />
                    </jsp:include>

                    <jsp:include page="/App/web/shared/card.jsp" >
                        <jsp:param name="iconCard" value="las la-envelope" />
                        <jsp:param name="titleCard" value="Email" />
                        <jsp:param name="descriptionCard" value="Configura todo lo referente a la configuración interna de email de la app." />
                        <jsp:param name="urlCard" value="#" />
                    </jsp:include>

                    <jsp:include page="/App/web/shared/card.jsp" >
                        <jsp:param name="iconCard" value="las la-users-cog" />
                        <jsp:param name="titleCard" value="Usuarios" />
                        <jsp:param name="descriptionCard" value="Visualiza y administra todos los usuarios registrado en la app." />
                        <jsp:param name="urlCard" value="<%= UrlAdmin.USER_LISTA.getUri() %>" />
                    </jsp:include>

                </div>

            </div>
            </div> <!-- Fin contenedor__formulario -->
        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>


    </div>
</div>







<%@ include file="/App/web/shared/foot.jsp" %>

<%@ include file="../shared/foot.jsp" %>