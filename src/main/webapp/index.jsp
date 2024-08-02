<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Index" />
</jsp:include>


<%@ include file="App/web/inicio/inicio.jsp" %>


<%@ include file="App/web/shared/foot.jsp" %>