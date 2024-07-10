<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Pruebas mysql" />
</jsp:include>


<%@ page import="io.proinstala.wherefind.infraestructure.data.*" %>
<%@ page import="io.proinstala.wherefind.shared.Dto.*" %>
<%@ page import="java.util.List" %>

<%
    GestionPersistencia gestionGlobal = new GestionPersistencia();
    IGestorPersistencia gestor = gestionGlobal.getGestorPersistencia();

    List<UserDto> listadoUsuarios = gestor.UsersGetAll();
    int total = listadoUsuarios.size();


    UserDto datosDavid = gestor.UsersGetUser("david", "123");
    UserDto datosJuanmaErroneo = gestor.UsersGetUser("juanma", "12300000000000000");

%>


<h1>Usuarios  <%= total %></h1>
<table border="1">
    <tr>
        <th>Nombre</th>
        <th>Password</th>
        <th>Rol</th>
    </tr>

    <%
    for (UserDto user : listadoUsuarios) {
        out.println("<tr><td>"+user.getUserName()+"</td> <td>"+user.getPassword()+"</td> <td>"+user.getRol()+"</td> </tr>");
    }
    %>

</table>

<hr/>

<%
    if(datosDavid==null)  {
%>
        <b>No he encontrado al uuario david o los datos introducidos son incorrectos</b> <br/>
<%
    } else {
%>
        out.println("<b>"+user.getUserName()+" : </b> <span>"+user.getPassword()+"</span> <span>"+user.getRol()+"</span> <br/>");
<%
    }
%>

<hr/>

<%
    if(datosJuanmaErroneo==null)  {
%>
        <b>No he encontrado al uuario juanma o los datos introducidos son incorrectos</b> <br/>
<%
    } else {
%>
        out.println("<b>"+user.getUserName()+" : </b> <span>"+user.getPassword()+"</span> <span>"+user.getRol()+"</span> <br/>");
<%
    }
%>




<%@ include file="App/web/shared/foot.jsp" %>