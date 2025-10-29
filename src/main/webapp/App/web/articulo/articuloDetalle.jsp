<%-- 
    Document   : articuloDetalle
    Created on : 18 oct 2025, 12:15:02
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.config.AppSettings"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>
<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }
    
    UserDTO userDTO = UserSession.getUserLogin(request);
 
    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "articulo/articulos/detalle");
    int articulo_id = actionController.getIntFromParametros(1);
%>
<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Existencia" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">
            
            <input type="hidden" id="userRol" name="userRol" value="<%=userDTO.getRol()%>">

            <!-- Contenedor Existencia -------------------------------------------------------------------------------- -->
            <div class="contenedor__formulario formulario--3_filas max-width-120" name="contenedorDatos" id="contenedorArticulo">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Artículo</h1>
                    </div>
                    <div>
                        <h1>(Datos)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de articulo -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleArticulo" id="divDetalleArticulo">
                        <input type="hidden" name="articulo_id" id="articulo_id" value="<%=articulo_id%>">
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgArticulo">
                                <input type="hidden" name="imagenArticuloB64" id="imagenArticuloB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultArticulo.svg" id="imgArticulo" alt="imagen articulo">
                                </div>

                                <label for="inputImgArticulo" class="input_foto">
                                    <input type="file" name="inputImgArticulo" id="inputImgArticulo" accept="image/*" disabled>
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenArticulo"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombreArticulo" id="nombreArticulo" value="" readonly>
                            <label for="nombreArticulo">Nombre Artículo</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionArticulo" id="descripcionArticulo" value="" readonly>
                            <label for="descripcionArticulo">Descripción Artículo</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="marca" id="marca" value="" readonly>
                            <label for="marca">Marca</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="modelo" id="modelo" value="" readonly>
                            <label for="modelo">Modelo</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="referencia" id="referencia" value="" readonly>
                            <label for="referencia">Referencia</label>
                        </div>
                        
                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnArticulo" title="Ver Articulo" class="marcado" disabled><i class="las la-shapes" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencias"><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedores"><i class="las la-store" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (articulo) -->
            
            
            <!-- Contenedor Existencias -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--4_filas max-width-120" name="contenedorDatos" id="contenedorExistencia">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Artículo</h1>
                    </div>
                    <div>
                        <h1>(Existencias)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de existencias -->
                <div class="contenedor__tabla--botones">

                    <div class="form__btn_circle">
                        <button id="btnCrearExistencia" title="Crear"><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnEliminarExistencia" title="Eliminar" disabled><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnModificarExistencia" title="Modificar" disabled><i class="las la-pen"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnDetalleExistencia" title="Detalle" disabled><i class="las la-info"></i></button>
                    </div>
                </div>
                
                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaExistencias" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>SKU</th>
                                <th>Proveedor</th>
                                <th>Precio</th>
                                <th>Fecha Compra</th>
                                <th>Almacén</th>
                                <th>Emplazamiento</th>
                                <th>Anotación</th>
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
                        <button name="btnArticulo" title="Ver Articulo"><i class="las la-shapes" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencias" class="marcado" disabled><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedores"><i class="las la-store" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (existencias) -->
            
            
            <!-- Contenedor Proveedor -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--4_filas max-width-120" name="contenedorDatos" id="contenedorProveedor">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Artículo</h1>
                    </div>
                    <div>
                        <h1>(Proveedores)</h1>
                    </div>
                </div>

                <div class="contenedor__tabla--botones">
                    <div class="form__btn_circle">
                        <button id="btnCrearProveedor" title="Crear"><i class="las la-plus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnEliminarProveedor" title="Eliminar" disabled><i class="las la-minus"></i></button>
                    </div>
                    <div class="form__btn_circle">
                        <button id="btnModificarProveedor" title="Modificar" disabled><i class="las la-pen"></i></button>
                    </div>
                </div>
                
                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaProveedores" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Proveedor</th>
                                <th>Precio</th>
                                <th>Fecha Precio</th>
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
                        <button name="btnArticulo" title="Ver Articulo"><i class="las la-shapes" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencias"><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedores" class="marcado" disabled><i class="las la-store" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (proveedor) -->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/articulo/articuloDetalle.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>

