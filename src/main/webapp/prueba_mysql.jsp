<%@page import="io.proinstala.wherefind.infraestructure.data.interfaces.IUserService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.tools.Tools"%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Pruebas mysql" />
</jsp:include>


<%@ page import="io.proinstala.wherefind.infraestructure.data.*" %>
<%@ page import="io.proinstala.wherefind.shared.dtos.*" %>
<%@ page import="java.util.List" %>

<%
    IUserService gestor = GestionPersistencia.getUserService();

    // Crea un nuevo usuario
    UserDTO userTemporal = new UserDTO(-1, "manolo", "password", "dddddddddd");
    userTemporal = gestor.add(userTemporal);

    if(userTemporal != null)
    {
        // Actualiza el usuario
        userTemporal.setPassword("nuevo");
        userTemporal.setRol("User");
        gestor.update(userTemporal);
    }

    // Crea un nuevo usuario para eliminarlo
    UserDTO userTemporalBorrar = gestor.add(new UserDTO(-1, "paraBorrar", "borrar", "User"));

    if(userTemporalBorrar != null)
    {
        // Elimina el usuario
        gestor.delete(userTemporalBorrar);
    }

    // Lista todos los usuarios
    List<UserDTO> listadoUsuarios = gestor.getAllUsers();
    int total = listadoUsuarios.size();

    // Obtiene datos individuales de usuarios
    UserDTO datosDavid = gestor.getUser("david", "123");
    UserDTO datosJuanma = gestor.getUser("juanma", "123");
    UserDTO datosJuanmaError = gestor.getUser("juanma", "12300000000000000");
    UserDTO datosUserById = gestor.getUserById(3);
    UserDTO datosUserByIdError = gestor.getUserById(-3333);

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