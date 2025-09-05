<%-- 
    Document   : almacenEditar
    Created on : 1 sept 2025, 21:46:52
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.config.AppSettings"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    int almacen_id = -1;
    try {
        //direccion_id = Integer.parseInt(request.getParameter("idDireccion"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    if (almacen_id == -1)
    {
        ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "almacen/almacenes/edit");
        almacen_id = actionController.getIntFromParametros(1);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Proveedor" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_direccion">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Almacen</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de contacto -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarAlmacen" id="frmModificarAlmacen">
                        <input type="hidden" name="almacen_id" id="almacen_id" value="<%=almacen_id%>">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre del almacen" value="">
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcion" id="descripcion" placeholder="Introduce una descripción del almacen" value="">
                            <label for="descripcion">Descripción</label>
                        </div>

                        <div class="form__input col-span-2">
                            <select name="direccion" id="direccion">
                            </select>
                            <label for="direccion">Dirección</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarAlmacen" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosAlmacen" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/almacen/almacenEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>
