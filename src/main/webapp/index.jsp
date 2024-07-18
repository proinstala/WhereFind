<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.identidad.UserSession"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response));
%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Index" />
</jsp:include>


<a href="App/web/admin/users.jsp">Usuarios</a>

<%@ include file="App/web/inicio/inicio.jsp" %>


<%@ include file="App/web/shared/foot.jsp" %>