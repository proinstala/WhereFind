<%-- 
    Document   : articuloCrear
    Created on : 21 sept 2025, 12:22:15
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
    <jsp:param name="titleweb" value="WhereFind - Articulo"/>
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_contacto">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Crear Artículo</h1>
                    </div>
                </div>

                <!-- Formulario -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmCrearArticulo" id="frmCrearArticulo">
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgArticulo">
                                <input type="hidden" name="imagenArticuloB64" id="imagenArticuloB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultArticulo.svg" id="imgArticulo" alt="imagen artículo">
                                </div>

                                <label for="inputImgArticulo" class="input_foto">
                                    <input type="file" name="inputImgArticulo" id="inputImgArticulo" accept="image/*">
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenArticulo"></span>
                                </label>
                            </div>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre del artículo" value="">
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcion" id="descripcion" placeholder="Introduce la descripción del artículo" value="">
                            <label for="descripcion">Descripción</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="referencia" id="referencia" placeholder="Introduce la referencia del artículo" value="">
                            <label for="referencia">Referencia</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="marca" id="marca">
                            </select>
                            <label for="marca">Marca</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="modelo" id="modelo" placeholder="Introduce el modelo del artículo" value="">
                            <label for="modelo">Modelo</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="number" name="stockMinimo" id="stockMinimo" placeholder="Introduce stock mínimo para el artículo" value="">
                            <label for="stockMinimo">Stock Mínimo</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmCrearArticulo" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosArticulo" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario -->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/articulo/articuloCrear.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>
