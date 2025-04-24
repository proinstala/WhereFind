

<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>

<%
    // Comprueba si el usuario esta logueado y es administrador. Si no se cumple, manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response), true)){
        // Detiene la ejecución de este servlet
        return;
    }

    int provincia_id = -1;
    try {
        //direccion_id = Integer.parseInt(request.getParameter("idDireccion"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    if (provincia_id == -1)
    {
        ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "direccion/provincias/edit");
        provincia_id = actionController.getIntFromParametros(1);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Modificar Provincia" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_provincia">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Provincia</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de direccion -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarProvincia" id="frmModificarProvincia">
                        <input type="hidden" name="provincia_id" id="provincia_id" value="<%=provincia_id%>">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre de la provincia" value="">
                            <label for="nombre">Nombre</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarProvincia" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosProvincia" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/direccion/provinciaEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>