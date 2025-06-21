<%-- 
    Document   : proveedorCrear
    Created on : 9 jun 2025, 9:46:21
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
    <jsp:param name="titleweb" value="Proveedor"/>
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_contacto">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Crear Proveedor</h1>
                    </div>
                </div>

                <!-- Formulario -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmCrearProveedor" id="frmCrearProveedor">
                        
                        <div class="form__input grid-row-span-2">
                            <div>
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/store.svg" id="imgProveedor" alt="imagen proveedor">
                                </div>

                                <label for="inputImgProveedor" class="input_foto">
                                    <input type="file" name="btnImgProveedor" id="inputImgProveedor" accept="image/*">
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenProveedor"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre del proveedor" value="">
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcion" id="descripcion" placeholder="Introduce una descripción del proveedor" value="">
                            <label for="descripcion">Descripción</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="paginaWeb" id="paginaWeb" placeholder="Introduce la página web del proveedor"value="">
                            <label for="paginaWeb">Página Web</label>
                        </div>

                        <div class="form__input col-span-2">
                            <select name="direccion" id="direccion">
                            </select>
                            <label for="direccion">Dirección</label>
                        </div>

                        <input type="hidden" name="imagenProveedorB64" id="imagenProveedorB64">
                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmCrearProveedor" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosProveedor" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/proveedor/proveedorCrear.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>