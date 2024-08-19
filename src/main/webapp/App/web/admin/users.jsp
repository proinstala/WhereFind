<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
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
    <jsp:param name="titleweb" value="Usuarios" />
</jsp:include>


<link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">
        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario max-width-120" id="form_busqueda">

                <div class="contenedor__formulario--cabecera conBotones">
                    <div>
                        <h1>Usuarios</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button id="btnBuscar" title="Buscar"><i class="las la-search"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnBuscar" title="Crear"><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnBuscar" title="Borrar"><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnBuscar" title="Modificar"><i class="las la-pen"></i></button>
                    </div>
                </div>

                <div class="contenedor__tabla">
                    <table id="admin-list-users" class="tabla">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Activo</th>
                                <th>Usuario</th>
                                <th>Nombre</th>
                                <th>Apellidos</th>
                                <th>Rol</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div> <!-- Fin contenedor__formulario -->
        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>

<script src="App/js/comunes.mjs" type="module"></script>
<script src="App/js/admin.js" type="module"></script>

<script  type="module">
    import { adminLoadListUsers } from './App/js/admin.js';

    adminLoadListUsers("admin-list-users");
</script>



<%@ include file="/App/web/shared/foot.jsp" %>

<%@ include file="../shared/foot.jsp" %>