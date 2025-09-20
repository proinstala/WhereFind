<%-- 
    Document   : marcaEditar
    Created on : 15 sept 2025, 22:42:25
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "articulo/marcas/edit");
    int marca_id = actionController.getIntFromParametros(1);
    
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Marca" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_direccion">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Marca</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de contacto -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarMarca" id="frmModificarMarca">
                        <input type="hidden" name="marca_id" id="marca_id" value="<%=marca_id%>">
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgMarca">
                                <input type="hidden" name="imagenMarcaB64" id="imagenMarcaB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultMarca.svg" id="imgMarca" alt="imagen marca">
                                </div>

                                <label for="inputImgMarca" class="input_foto">
                                    <input type="file" name="inputImgMarca" id="inputImgMarca" accept="image/*">
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenMarca"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre de la marca" value="">
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcion" id="descripcion" placeholder="Introduce la descripción de la marca" value="">
                            <label for="descripcion">Descripción</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarMarca" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosMarca" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/articulo/marcaEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>

