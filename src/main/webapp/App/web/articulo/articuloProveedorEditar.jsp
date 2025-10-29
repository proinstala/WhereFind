<%-- 
    Document   : articuloProveedorEditar
    Created on : 25 oct 2025, 8:32:35
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    UserDTO userDTO = UserSession.getUserLogin(request);
    
    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "articulo/articulos/articuloProveedorEditar");
    int articuloProveedor_id = actionController.getIntFromParametros(1);
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
                <input type="hidden" name="articuloProveedor_id" id="articuloProveedor_id" value="<%=articuloProveedor_id%>">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Artículo -> Proveedor</h1>
                    </div>
                </div>

                <!-- Formulario -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmEditarArticuloProveedor" id="frmEditarArticuloProveedor">
                        
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
                            <input type="text" name="articulo" id="articulo" value="" readonly>
                            <label for="articulo">Articulo</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionArticulo" id="descripcionArticulo" value="" readonly>
                            <label for="descripcionArticulo">Descripción Artículo</label>
                        </div>
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgProveedor">
                                <input type="hidden" name="imagenProveedorB64" id="imagenProveedorB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultProveedor.svg" id="imgProveedor" alt="imagen proveedor">
                                </div>

                                <label for="inputImgProveedor" class="input_foto">
                                    <input type="file" name="inputImgProveedor" id="inputImgProveedor" accept="image/*" disabled>
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenProveedor"></span>
                                </label>
                            </div>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="proveedor" id="proveedor" value="" readonly>
                            <label for="proveedor">Proveedor</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionProveedor" id="descripcionProveedor" value="" readonly>
                            <label for="descripcionProveedor">Descripción Proveedor</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="number" name="precio" id="precio" placeholder="Introduce el precio del artículo" value="" min="0">
                            <label for="precio">Precio</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="date" name="fechaPrecio" id="fechaPrecio" value="">
                            <label for="fechaPrecio">Fecha Precio</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="disponibilidad" id="disponibilidad">
                                <option value="0" class="no_disponible">No Disponible</option>
                                <option value="1" class="disponible">Disponible</option>
                            </select>
                            <label for="disponibilidad">Disponibilidad</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="date" name="fechaNoDisponible" id="fechaNoDisponible" value="">
                            <label for="fechaNoDisponible">Fecha no disponible</label>
                        </div>
                        
                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmEditarArticuloProveedor" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosArticuloProveedor" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/articulo/articuloProveedorEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>