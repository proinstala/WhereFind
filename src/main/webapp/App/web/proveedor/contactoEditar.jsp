
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>

<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    int contacto_id = -1;
    try {
        //direccion_id = Integer.parseInt(request.getParameter("idDireccion"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    if (contacto_id == -1)
    {
        ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "proveedor/contactos/edit");
        contacto_id = actionController.getIntFromParametros(1);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Modificar Contacto" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_direccion">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Contacto</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de contacto -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarContacto" id="frmModificarContacto">
                        <input type="hidden" name="contacto_id" id="contacto_id" value="<%=contacto_id%>">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre del contacto" value="">
                            <label for="nombre">Nombre</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="apellido" id="apellido" placeholder="Introduce el apellido del contacto"value="">
                            <label for="numero">Apellido</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="telefono" id="telefono" placeholder="Introduce el telefono"value="">
                            <label for="telefono">Teléfono</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="email" id="email" placeholder="Introduce el email"value="">
                            <label for="email">Email</label>
                        </div>

                        <div class="form__input">
                            <select name="puesto" id="puesto">
                            </select>
                            <label for="puesto">Puesto</label>
                        </div>

                        <div class="form__input">
                            <select name="proveedor" id="proveedor">
                            </select>
                            <label for="localidad">Proveedor</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarContacto" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosContacto" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/proveedor/contactoEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>