<%@page import="io.proinstala.wherefind.shared.tools.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Pruebas mysql" />
</jsp:include>


<%@ page import="io.proinstala.wherefind.infraestructure.data.*" %>
<%@ page import="io.proinstala.wherefind.shared.dto.*" %>
<%@ page import="java.util.List" %>

<%
    GestionPersistencia gestionGlobal = new GestionPersistencia();
    IGestorPersistencia gestor = gestionGlobal.getGestorPersistencia();

    // Crea un nuevo usuario
    UserDTO userTemporal = new UserDTO("manolo", "password", "dddddddddd");
    userTemporal = gestor.UsersAdd(userTemporal);

    if(userTemporal != null)
    {
        // Actualiza el usuario
        userTemporal.setPassword("nuevo");
        userTemporal.setRol("User");
        gestor.UsersUpdate(userTemporal);
    }

    // Crea un nuevo usuario para eliminarlo
    UserDTO userTemporalBorrar = gestor.UsersAdd(new UserDTO("paraBorrar", "borrar", "User"));

    if(userTemporalBorrar != null)
    {
        // Elimina el usuario
        gestor.UsersDelete(userTemporalBorrar);
    }

    // Lista todos los usuarios
    List<UserDTO> listadoUsuarios = gestor.UsersGetAll();
    int total = listadoUsuarios.size();

    // Obtiene datos individuales de usuarios
    UserDTO datosDavid = gestor.UsersGetUser("david", "123");
    UserDTO datosJuanma = gestor.UsersGetUser("juanma", "123");
    UserDTO datosJuanmaError = gestor.UsersGetUser("juanma", "12300000000000000");
    UserDTO datosUserById = gestor.UsersGetUserById(3);
    UserDTO datosUserByIdError = gestor.UsersGetUserById(-3333);

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
    for (UserDTO user : listadoUsuarios) {
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