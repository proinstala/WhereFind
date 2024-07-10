<%@page import="io.proinstala.wherefind.shared.tools.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Pruebas mysql" />
</jsp:include>


<%@ page import="io.proinstala.wherefind.infraestructure.data.*" %>
<%@ page import="io.proinstala.wherefind.shared.Dto.*" %>
<%@ page import="java.util.List" %>

<%
    GestionPersistencia gestionGlobal = new GestionPersistencia();
    IGestorPersistencia gestor = gestionGlobal.getGestorPersistencia();

    // Crea un nuevo usuario
    UserDto userTemporal = new UserDto("manolo", "password", "dddddddddd");
    userTemporal = gestor.UsersAdd(userTemporal);

    if(userTemporal != null)
    {
        // Actualiza el usuario
        userTemporal.setPassword("nuevo");
        userTemporal.setRol("User");
        gestor.UsersUpdate(userTemporal);
    }


    // Lista todos los usuarios
    List<UserDto> listadoUsuarios = gestor.UsersGetAll();
    int total = listadoUsuarios.size();

    // Obtiene datos individuales de usuarios
    UserDto datosDavid = gestor.UsersGetUser("david", "123");
    UserDto datosJuanma = gestor.UsersGetUser("juanma", "123");
    UserDto datosJuanmaError = gestor.UsersGetUser("juanma", "12300000000000000");
    UserDto datosUserById = gestor.UsersGetUserById(3);
    UserDto datosUserByIdError = gestor.UsersGetUserById(-3333);

    String resultadoUserDavid = Tools.getMensajeResultado(datosDavid);
    String resultadoUserJuanma = Tools.getMensajeResultado(datosJuanma);
    String resultadoUserJuanmaError = Tools.getMensajeResultado(datosJuanmaError);
    String resultadoUserById = Tools.getMensajeResultado(datosUserById);
    String resultadoUserByIdError = Tools.getMensajeResultado(datosUserByIdError);
%>


<h1>Usuarios  <%= total %></h1>
<table border="1">
    <tr>
        <th>Id</th>
        <th>Nombre</th>
        <th>Password</th>
        <th>Rol</th>
    </tr>

    <%
    for (UserDto user : listadoUsuarios) {
        out.println("<tr><td>"+user.getId()+"</td> <td>"+user.getUserName()+"</td> <td>"+user.getPassword()+"</td> <td>"+user.getRol()+"</td> </tr>");
    }
    %>

</table>

<hr/>
<%= resultadoUserDavid %>
<hr/>
<%= resultadoUserJuanmaError %>
<hr/>
<%= resultadoUserJuanma %>
<hr/>
<%= resultadoUserById %>
<hr/>
<%= resultadoUserByIdError %>






<%@ include file="App/web/shared/foot.jsp" %>