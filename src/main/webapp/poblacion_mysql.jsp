<%--
    Document   : poblacion_mysql
    Created on : 17 jul 2024, 13:47:37
    Author     : Administrador
--%>

<%@page import="java.util.List"%>
<%@page import="io.proinstala.wherefind.shared.dtos.PoblacionDTO"%>
<%@page import="io.proinstala.wherefind.infraestructure.data.interfaces.IPoblacionService"%>
<%@page import="io.proinstala.wherefind.infraestructure.data.GestionPersistencia"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>


<%
    IPoblacionService gestor = GestionPersistencia.getPoblacionService();

    // Crea un nueva poblacion
    PoblacionDTO temporal = new PoblacionDTO(-1, "Cartagena");
    temporal = gestor.add(temporal);


    // Lista todas los poblaciones
    List<PoblacionDTO> listado = gestor.getAllPoblaciones();
    int total = listado.size();
%>


<h1>Poblaciones  <%= total %></h1>
<table border="1">
    <tr>
        <th>Id</th>
        <th>Nombre</th>
    </tr>

    <%
    for (PoblacionDTO poblacion : listado) {
        out.println("<tr><td>"+poblacion.getId()+"</td> <td>"+poblacion.getName()+"</td> </tr>");
    }
    %>

</table>



    </body>
</html>
