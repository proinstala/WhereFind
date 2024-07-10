<%
    // Si no se está logueado se manda al usuario al login.jsp
    UserSession.RedireccionarIsUserNotLogIn(request, response);
%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Index" />
</jsp:include>

<%@page import="io.proinstala.wherefind.shared.dto.UserDto"%>
<%@page import="io.proinstala.wherefind.Identity.UserSession"%>

<%@ include file="App/web/cabecera/cabecera.jsp" %>

<%
    UserDto usuarioActual = UserSession.GetUserLogin(request);
    if (usuarioActual != null)
    {
        out.println("<h1>Bienvenido " + usuarioActual.getUserName() + "</h1>");
    }
    else
    {
        out.println("<h1>No hay nadie logueado...</h1>");
    }
%>



<%@ include file="App/web/shared/foot.jsp" %>