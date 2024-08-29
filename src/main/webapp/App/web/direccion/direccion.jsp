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
    <jsp:param name="titleweb" value="Dirección"/>
</jsp:include>

<link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--4_filas max-width-120" id="form_busqueda">

                <div class="contenedor__formulario--cabecera conBotones">
                    <div>
                        <h1>Dirección</h1>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmBuscarDireccion" id="frmBuscarDireccion">
                        
                        <div class="form__input">
                            <input type="text" name="calle" id="calle">
                            <label for="calle">Calle</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="provincia" id="provincia">
                                <option value="-1">Todas</option>
                            </select>
                            <label for="provincia">Provincia</label>
                        </div>
                        
                        <div class="form__input">
                            <select name="localidad" id="localidad">
                                <option value="-1">Todas</option>
                            </select>
                            <label for="localidad">Localidad</label>
                        </div>
                        
                    </form>
                </div>

                <div class="contenedor__tabla--botones">
                    
                    <div class="form__btn_circle">
                        <button form="frmBuscarDireccion" id="btnBuscar" title="Buscar" type="submit"><i class="las la-search"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnCrear" title="Crear"><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnEliminar" title="Eliminar"><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnModificar" title="Modificar"><i class="las la-pen"></i></button>
                    </div>
                </div>
                
                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaDireciones" data-rowSelected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Calle</th>
                                <th>Número</th>
                                <th>Codigo Posta</th>
                                <th>Localidad</th>
                                <th>Provincia</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- El contenido se cargar desde javaScript -->
                        </tbody>
                    </table>
                </div>
                
                <!-- boton en footer con posicionamiento stiky
                <div class="contenedor__formulario--footer sticky">
                    
                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>
                -->

            </div> <!-- Fin contenedor__formulario -->
            
        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>

<script src="App/js/direccion.js" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>