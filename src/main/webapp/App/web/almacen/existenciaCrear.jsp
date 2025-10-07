<%-- 
    Document   : existenciaCrear
    Created on : 26 sept 2025, 19:17:19
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.config.AppSettings"%>
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
    <jsp:param name="titleweb" value="WherFind - Existencia"/>
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_existencia">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Crear Existencia</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de direccion -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmCrearExistencia" id="frmCrearExistencia">
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgArticulo">
                                <input type="hidden" name="imagenArticuloB64" id="imagenArticuloB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultArticulo.svg" id="imgArticulo" alt="imagen artículo">
                                </div>

                                <label for="inputImgArticulo" class="input_foto">
                                    <input type="file" name="inputImgArticulo" id="inputImgArticulo" accept="image/*" disabled>
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenArticulo"></span>
                                </label>
                            </div>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <select name="articulo" id="articulo">
                            </select>
                            <label for="articulo">Artículo</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="articuloProveedor" id="articuloProveedor">
                            </select>
                            <label for="articuloProveedor">Proveedor</label>
                        </div>

                        <div class="form__input">
                            <input type="number" name="precio" id="precio" placeholder="Introduce el precio" value="">
                            <label for="precio">Precio</label>
                        </div>

                        <div class="form__input">
                            <input type="date" name="fechaCompra" id="fechaCompra" value="">
                            <label for="fechaCompra">Fecha de Compra</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="comprador" id="comprador" placeholder="Introduce el nombre del comprador"value="">
                            <label for="comprador">Comprador</label>
                        </div>

                        <div class="form__input">
                            <select name="almacen" id="almacen">
                            </select>
                            <label for="almacen">Almacén</label>
                        </div>

                        <div class="form__input">
                            <select name="emplazamiento" id="emplazamiento">
                            </select>
                            <label for="emplazamiento">Emplazamiento</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmCrearExistencia" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosExistencia" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (direccion)-->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/almacen/existenciaCrear.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>