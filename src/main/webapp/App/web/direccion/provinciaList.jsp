<%-- 
    Document   : provinciaList
    Created on : 15 mar 2025, 11:30:45
    Author     : David
--%>


<%@page import="io.proinstala.wherefind.shared.dtos.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    UserDTO userDTO = UserSession.getUserLogin(request);
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Provincia"/>
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">
            
            <input type="hidden" id="userRol" name="userRol" value="<%=userDTO.getRol()%>">
            
            <div class="contenedor__formulario formulario--4_filas max-width-120" id="form_busqueda">

                <div class="contenedor__formulario--cabecera conBotones">
                    <div>
                        <h1>Provincias</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmBuscarProvincia" id="frmBuscarProvincia">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre">
                            <label for="nombre">Nombre</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button form="frmBuscarProvincia" id="btnBuscar" title="Buscar" type="submit"><i class="las la-search"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCrear" title="Crear" disabled><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnEliminar" title="Eliminar" disabled><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnModificar" title="Modificar" disabled><i class="las la-pen"></i></button>
                    </div>
                </div>

                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaProvincias" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- El contenido se cargar desde javaScript -->
                        </tbody>
                    </table>
                </div>

            </div> <!-- Fin contenedor__formulario -->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>

<script src="App/js/direccion/provinciaList.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>