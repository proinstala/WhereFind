<%-- 
    Document   : contactoCrear
    Created on : 14 may 2025, 18:56:18
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
    <jsp:param name="titleweb" value="Contacto"/>
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_contacto">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Crear Contactos</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de direccion -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmCrearContacto" id="frmCrearContacto">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre del contacto" value="">
                            <label for="nombre">Nombre</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="apellido" id="apellido" placeholder="Introduce el apellid del contacto"value="">
                            <label for="apellido">Apellido</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="telefono" id="telefono" placeholder="Introduce el telefono del contacto"value="">
                            <label for="telefono">Telefono</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="email" name="email" id="email" placeholder="Introduce el email del contacto"value="">
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
                            <label for="proveedor">Proveedor</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmCrearContacto" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
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


<script src="App/js/proveedor/contactoCrear.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>