
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

    int direccion_id = -1;
    try {
        //direccion_id = Integer.parseInt(request.getParameter("idDireccion"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    if (direccion_id == -1)
    {
        ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "direccion/direcciones/edit");
        direccion_id = actionController.getIntFromParametros(1);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Modificar Dirección" />
</jsp:include>

<link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_direccion">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Dirección</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos de direccion -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarDireccion" id="frmModificarDireccion">
                        <input type="hidden" name="direccion_id" id="direccion_id" value="<%=direccion_id%>">

                        <div class="form__input">
                            <input type="text" name="calle" id="calle" placeholder="Introduce el nombre de la calle" value="">
                            <label for="calle">Calle</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="numero" id="numero" placeholder="Introduce el número"value="">
                            <label for="numero">Número</label>
                        </div>

                        <div class="form__input">
                            <input type="number" name="codigoPostal" id="codigoPostal" placeholder="Introduce el código postal"value="">
                            <label for="codigoPostal">Código Postal</label>
                        </div>

                        <div class="form__input">
                            <select name="provincia" id="provincia">
                                <option value="-1">Selecciona</option>
                            </select>
                            <label for="provincia">Provincia</label>
                        </div>

                        <div class="form__input">
                            <select name="localidad" id="localidad">
                                <option value="-1">Selecciona</option>
                            </select>
                            <label for="localidad">Localidad</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button form="frmModificarDireccion" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosDireccion" title="Deshacer cambios" disabled><i class="las la-redo-alt" ></i></button>
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


<script src="App/js/direccion/direccionEditar.js" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>