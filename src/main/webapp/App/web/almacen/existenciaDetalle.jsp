<%-- 
    Document   : existenciaDetalle
    Created on : 10 oct 2025, 18:19:32
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
 
    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "almacen/existencias/detalle");
    int existencia_id = actionController.getIntFromParametros(1);
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

            <!-- Contenedor Existencia -------------------------------------------------------------------------------- -->
            <div class="contenedor__formulario formulario--3_filas max-width-120" name="contenedorDatos" id="contenedorExistencia">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Existencia</h1>
                    </div>
                    <div>
                        <h1>(Datos)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de existencia -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleExistencia" id="divDetalleExistencia">
                        <input type="hidden" name="existencia_id" id="existencia_id" value="<%=existencia_id%>">
                        
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
                        
                        <div class="form__input">
                            <input type="text" name="sku" id="sku" value="" readonly>
                            <label for="sku">SKU</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="number" name="precio" id="precio" value="" readonly>
                            <label for="precio">Precio</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="date" name="fechaCompra" id="fechaCompra" value="" readonly>
                            <label for="fechaCompra">Fecha de Compra</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="comprador" id="comprador" value="" readonly>
                            <label for="comprador">Comprador</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="disponibilidad" id="disponibilidad" value="" readonly>
                            <label for="disponibilidad">Disponibilidad</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="date" name="fechaNoDisponible" id="fechaNoDisponible" value="" readonly>
                            <label for="fechaNoDisponible">Fecha no disponible</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="anotacion" id="anotacion" value="" readonly>
                            <label for="sku">Anotación</label>
                        </div>
                        

                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencia" class="marcado" disabled><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento"><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedor"><i class="las la-store" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (existencia) -->
            
            
            <!-- Contenedor Emplazamiento -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--3_filas max-width-120" name="contenedorDatos" id="contenedorEmplazamiento">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Existencia</h1>
                    </div>
                    <div>
                        <h1>(Emplazamiento)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de proveedor -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleEmplazamiento" id="divDetalleEmplazamiento">

                        <div class="form__input">
                            <input type="text" name="nombreAlmacen" id="nombreAlmacen" value="" readonly>
                            <label for="nombreAlmacen">Nombre Almacén</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionAlmacen" id="descripcionAlmacen" value="" readonly>
                            <label for="descripcionAlmacen">Descripción Almacén</label>
                        </div>

                        <div class="form__input col-span-all">
                            <input type="text" name="direccionAlmacen" id="direccionAlmacen" value="" readonly>
                            <label for="direccionAlmacen">Dirección Almacen</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="nombreEmplazamiento" id="nombreEmplazamiento" value="" readonly>
                            <label for="nombreAlmacen">Nombre Emplazamiento</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionEmplazamiento" id="descripcionEmplazamiento" value="" readonly>
                            <label for="descripcionEmplazamiento">Descripción Emplazamiento</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="tipoEmplazamiento" id="tipoEmplazamiento" value="" readonly>
                            <label for="tipoEmplazamiento">Tipo Emplazamiento</label>
                        </div>
                        
                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencia"><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento" class="marcado" disabled><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedor"><i class="las la-store" ></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (emplazamiento) -->
            
            
            <!-- Contenedor Proveedor -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--3_filas max-width-120" name="contenedorDatos" id="contenedorProveedor">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Existencia</h1>
                    </div>
                    <div>
                        <h1>(Proveedor)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de proveedor -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleProveedor" id="divDetalleProveedor">
                        
                        <div class="form__input grid-row-span-2">
                            <div id="contenedorImgProveedor">
                                <input type="hidden" name="imagenProveedorB64" id="imagenProveedorB64">
                                <div class="contenedor__formulario--imagen">
                                    <img src="App/img/defaultProveedor.svg" id="imgProveedor" alt="imagen proveedor">
                                </div>

                                <label for="inputImgProveedor" class="input_foto">
                                    <input type="file" name="inputImgProveedor" id="inputImgProveedor" accept="image/*" disabled>
                                    <i class="las la-camera"></i>
                                    <span id="textoImagenProveedor"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombreProveedor" id="nombreProveedor" value="" readonly>
                            <label for="nombreProveedor">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcionProveedor" id="descripcionProveedor" value="" readonly>
                            <label for="descripcionProveedor">Descripción</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="paginaWeb" id="paginaWeb" value="" readonly>
                            <label for="paginaWeb">Página Web</label>
                        </div>
                        

                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnExistencia" title="Ver Existencia"><i class="las la-puzzle-piece" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnEmplazamiento" title="Ver Emplazamiento"><i class="las la-database" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnProveedor" title="Ver Proveedor" class="marcado" disabled><i class="las la-store" ></i></button>
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


<script src="App/js/almacen/existenciaDetalle.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>
