<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.identidad.UserSession"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    //UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response));
%>

<jsp:include page="../shared/head.jsp" >
    <jsp:param name="titleweb" value="Usuarios" />
</jsp:include>



<h1>Usuarios</h1>

<table id="admin-list-users" border="1">
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


<script src="App/js/comunes.mjs" type="module"></script>
<script src="App/js/admin.js" type="module"></script>

<script  type="module">
    import { adminLoadListUsers } from './App/js/admin.js';

    const adminLoadListUsersCallBack = (response)  => {
        console.log("Respuesta a adminLoadListUsersCallBack")
        console.log(response);
        console.log(response.user.length);

        var tableRef = document.getElementById('admin-list-users').getElementsByTagName('tbody')[0];

        for (var j = 0; j < response.user.length; j++){
            tableRef.insertRow().innerHTML =
                "<td>" + response.user[j].id + "</td>" +
                "<td>" + response.user[j].userName + "</td>"+
                "<td>" +response.user[j].nombre+ "</td>"+
                "<td>" +response.user[j].apellidos+ "</td>"+
                "<td>" +response.user[j].rol+ "</td>";
        }

        //$("#admin-list-users").find('tbody').append("<tr><td>aaaa</td></tr>");
    }

    adminLoadListUsers("#admin-list-users", adminLoadListUsersCallBack);
</script>

<%@ include file="../shared/foot.jsp" %>