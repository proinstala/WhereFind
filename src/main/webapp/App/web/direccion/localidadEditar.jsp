<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>

<%
    // Comprueba si el usuario está logueado y es administrador. Si no se cumple, manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response), true)){
        // Detiene la ejecución de este servlet
        return;
    }

    int localidad_id = -1;
    try {
        //localidad_id = Integer.parseInt(request.getParameter("idDireccion"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    if (localidad_id == -1)
    {
        ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "direccion/localidades/edit");
        localidad_id = actionController.getIntFromParametros(1);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Modificar Localidad" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_localidad">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Localidad</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de localidad -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarLocalidad" id="frmModificarLocalidad">
                        <input type="hidden" name="localidad_id" id="localidad_id" value="<%=localidad_id%>">

                        <div class="form__input">
                            <input type="text" name="nombre" id="nombre" placeholder="Introduce el nombre de la localidad" value="">
                            <label for="calle">Calle</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="provincia" id="provincia">
                                <option value="-1">Selecciona</option>
                            </select>
                            <label for="provincia">Provincia</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarLocalidad" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosLocalidad" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (localidad)-->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/direccion/localidadEditar.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>