<%@page contentType="text/html" pageEncoding="UTF-8"%>


<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Registrar" />
</jsp:include>

<link rel="stylesheet" href="App/css/registrar.css">

<div class="contenedor__general--registrar">
    <div class="contenedor__formulario max-width-80">

        <div class="contenedor__formulario--cabecera">
            <div>
                <h1>Cambie el password</h1>
            </div>
        </div>


Usuario que quiere cambiar el password : ${userDTO.getUserName()}


<%@ include file="/App/web/shared/foot.jsp" %>


