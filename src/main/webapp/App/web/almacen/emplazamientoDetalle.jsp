<%-- 
    Document   : emplazamientoDetalle
    Created on : 17 oct 2025, 17:50:04
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.dtos.UserDTO"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }
    
    UserDTO userDTO = UserSession.getUserLogin(request);

    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "almacen/emplazamientos/detalle");
    int emplazamiento_id = actionController.getIntFromParametros(1);
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Emplazamiento" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">
            
            <input type="hidden" id="userRol" name="userRol" value="<%=userDTO.getRol()%>">

            <!-- Contenedor Emplazamiento -------------------------------------------------------------------------------- -->
            <div class="contenedor__formulario formulario--3_filas max-width-100" name="contenedorDatos" id="contenedorEmplazamiento">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Emplazamiento</h1>
                    </div>
                    <div>
                        <h1>(Datos)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de emplazamiento -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleEmplazamiento" id="divDetalleEmplazamiento">
                        <input type="hidden" name="emplazamiento_id" id="emplazamiento_id" value="<%=emplazamiento_id%>">

                        <div class="form__input">
                            <input type="text" name="nombreEmplazamiento" id="nombreEmplazamiento" value="" readonly>
                            <label for="nombreEmplazamiento">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionEmplazamiento" id="descripcionEmplazamiento" value="" readonly>
                            <label for="descripcionEmplazamiento">Descripción</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="tipoEmplazamiento" id="tipoEmplazamiento" value="" readonly>
                            <label for="tipoEmplazamiento">Tipo</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="totalExistencias" id="totalExistencias" value="" readonly>
                            <label for="totalExistencias">Total Existencias</label>
                        </div>
                        
                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento" class="marcado" disabled><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnAlmacen" title="Ver Almacén"><i class="las la-box" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnExistencias" title="Ver Existencias"><i class="las la-puzzle-piece"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (emplazamiento) -->
            
            
            <!-- Contenedor Direccion -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--3_filas max-width-100" name="contenedorDatos" id="contenedorAlmacen">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Emplazamiento</h1>
                    </div>
                    <div>
                        <h1>(Almacén)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de almacen -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleAlmacen" id="divDetalleAlmacen">

                        <div class="form__input">
                            <input type="text" name="nombreAlmacen" id="nombreAlmacen" value="" readonly>
                            <label for="nombreAlmacen">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionAlmacen" id="descripcionAlmacen" value="" readonly>
                            <label for="descripcionAlmacen">Descripción</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="direccionAlmacen" id="direccionAlmacen" value="" readonly>
                            <label for="direccionAlmacen">Dirección</label>
                        </div>

                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento"><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnAlmacen" title="Ver Almacén" class="marcado" disabled><i class="las la-box" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnExistencias" title="Ver Existencias"><i class="las la-puzzle-piece"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (almacen)-->
            
            
            <!-- Contenedor Contacto -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--4_filas max-width-100" name="contenedorDatos" id="contenedorExistencias">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Emplazamiento</h1>
                    </div>
                    <div>
                        <h1>(Existencias)</h1>
                    </div>
                </div>

                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button id="btnCrear" title="Crear"><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnEliminar" title="Eliminar" disabled><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnModificar" title="Modificar" disabled><i class="las la-pen"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnDetalle" title="Detalle" disabled><i class="las la-info"></i></button>
                    </div>
                </div>
                
                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaExistencias" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Articulo</th>
                                <th>Referencia</th>
                                <th>Marca</th>
                                <th>SKU</th>
                                <th>Disponible</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- El contenido se cargar desde javaScript -->
                        </tbody>
                    </table>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento"><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnAlmacen" title="Ver Almacén"><i class="las la-box" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnExistencias" title="Ver Existencias" class="marcado" disabled><i class="las la-puzzle-piece"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (existencias)-->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/almacen/emplazamientoDetalle.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>
