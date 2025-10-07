<%-- 
    Document   : existenciaList
    Created on : 26 sept 2025, 19:13:10
    Author     : David
--%>

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
    <jsp:param name="titleweb" value="WhereFind - Existencia"/>
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
                        <h1>Existecias</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmBuscarExistencia" id="frmBuscarExistencia">

                        <div class="form__input">
                            <input type="text" name="articulo" id="articulo">
                            <label for="articulo">Artículo</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="referencia" id="referencia">
                            <label for="referencia">Referencia</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="marca" id="marca">
                            </select>
                            <label for="marca">Marca</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="almacen" id="almacen">
                            </select>
                            <label for="almacen">Almacén</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="nombreEmplazamiento" id="nombreEmplazamiento">
                            <label for="nombreEmplazamiento">Emplazamiento</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="disponible" id="disponible">
                                <option value="-1">Todos</option>
                                <option value="0">No disponible</option>
                                <option value="1">Disponible</option>
                            </select>
                            <label for="disponible">Disponibilidad</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button form="frmBuscarExistencia" id="btnBuscar" title="Buscar" type="submit"><i class="las la-search"></i></button>
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
                    <table class="tabla" id="tablaExistencias" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Artículo</th>
                                <th>Descripcion</th>
                                <th>Referencia</th>
                                <th>Marca</th>
                                <th>Almacen</th>
                                <th>Emplazamiento</th>
                                <th>Disponible</th>
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

<script src="App/js/almacen/existenciaList.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>