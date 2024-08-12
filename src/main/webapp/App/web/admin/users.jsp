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

<div class="contenedor__general">
    <div class="contenedor">
        <%@ include file="../shared/cabecera.jsp" %>
                <div class="seccion">


<h1>Usuarios</h1>
<table id="admin-list-users" border="0" style="border: 1px solid gray;">
    <tr>
        <th>Id</th>
        <th>Usuario</th>
        <th>Nombre</th>
        <th>Apellidos</th>
        <th>Rol</th>
    </tr>
    <tbody>
    </tbody>
</table>


<h4>Esta apariencia es temporal. Esta vista solo es para poder testear que no se puede acceder a ella si no se es Administrador.</h4>

    </div>
                </div>
</div>

<script src="App/js/comunes.mjs" type="module"></script>
<script src="App/js/admin.js" type="module"></script>

<script  type="module">
    import { adminLoadListUsers } from './App/js/admin.js';

    adminLoadListUsers("admin-list-users");
</script>




<%@ include file="../shared/foot.jsp" %>