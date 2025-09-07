<%-- 
    Document   : emplazamientoList
    Created on : 6 sept 2025, 9:51:50
    Author     : judas
--%>

<%@page import="io.proinstala.wherefind.shared.config.AppSettings"%>
<%@page import="io.proinstala.wherefind.shared.dtos.UserDTO"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    UserDTO userDTO = UserSession.getUserLogin(request);
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Emplazamiento"/>
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
                        <h1>Emplazamientos</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmBuscarEmplazamiento" id="frmBuscarEmplazamiento">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre">
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="descripcion" id="descripcion">
                            <label for="descripcion">Descripción</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="tipo" id="tipo">
                            </select>
                            <label for="tipo">Tipo</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button form="frmBuscarEmplazamiento" id="btnBuscar" title="Buscar" type="submit"><i class="las la-search"></i></button>
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
                    <div class="form__btn_circle">
                        <button id="btnDetalle" title="Detalle" disabled><i class="las la-info"></i></button>
                    </div>
                </div>

                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaEmplazamiento" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Descripcion</th>
                                <th>Tipo</th>
                                <th>Almacen</th>
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

<script src="App/js/almacen/emplazamientoList.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>