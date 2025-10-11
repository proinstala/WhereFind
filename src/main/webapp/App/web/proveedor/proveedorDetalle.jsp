<%-- 
    Document   : proveedorDetalle
    Created on : 2 jul 2025, 19:32:12
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.controllers.BaseHttpServlet"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionController"%>
<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    ActionController actionController = BaseHttpServlet.getActionControllerFromJSP(request, response, "proveedor/proveedores/detalle");
    int proveedor_id = actionController.getIntFromParametros(1);
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="WhereFind - Proveedor" />
</jsp:include>

<link href="App/css/formulario.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>
<link href="App/css/tabla.css?v=<%=AppSettings.APP_VERSION_CSS%>" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <!-- Contenedor Proveedor -------------------------------------------------------------------------------- -->
            <div class="contenedor__formulario formulario--3_filas max-width-100" name="contenedorDatos" id="contenedorProveedor">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Proveedor</h1>
                    </div>
                    <div>
                        <h1>(Datos)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de proveedor -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleProveedor" id="divDetalleProveedor">
                        <input type="hidden" name="proveedor_id" id="proveedor_id" value="<%=proveedor_id%>">
                        
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
                            <input type="text" name="nombre" id="nombre" value="" readonly>
                            <label for="nombre">Nombre</label>
                        </div>
                        
                        <div class="form__input col-span-2">
                            <input type="text" name="descripcion" id="descripcion" value="" readonly>
                            <label for="descripcion">Descripción</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="paginaWeb" id="paginaWeb" value="" readonly>
                            <label for="paginaWeb">Página Web</label>
                        </div>
                        
                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle">
                        <button name="btnProveedor" title="Ver Proveedor" class="marcado" disabled><i class="las la-store" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnDireccion" title="Ver Direccion"><i class="las la-map-marker-alt"></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnContactos" title="Ver Contactos"><i class="las la-id-card"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (proveedor) -->
            
            
            <!-- Contenedor Direccion -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--3_filas max-width-100" name="contenedorDatos" id="contenedorDireccion">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Proveedor</h1>
                    </div>
                    <div>
                        <h1>(Dirección)</h1>
                    </div>
                </div>

                <!-- Formulario para ver los datos de direccion -->
                <div class="contenedor__formulario--main">
                    <div class="formulario" name="divDetalleDireccion" id="divDetalleDireccion">

                        <div class="form__input">
                            <input type="text" name="calle" id="calle" value="" readonly>
                            <label for="calle">Calle</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="numero" id="numero" value="" readonly>
                            <label for="numero">Número</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="codigoPostal" id="codigoPostal" value="" readonly>
                            <label for="codigoPostal">Código Postal</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="localidad" id="localidad" value="" readonly>
                            <label for="Localidad">Localidad</label>
                        </div>
                        
                        <div class="form__input">
                            <input type="text" name="provincia" id="provincia" value="" readonly>
                            <label for="provincia">Provincia</label>
                        </div>

                    </div>
                </div>

                <div class="contenedor__formulario--footer">
                    
                    <div class="form__btn_circle">
                        <button name="btnProveedor" title="Ver Proveedor"><i class="las la-store" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnDireccion" title="Ver Direccion" class="marcado" disabled><i class="las la-map-marker-alt"></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnContactos" title="Ver Contactos"><i class="las la-id-card"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (direccion)-->
            
            
            <!-- Contenedor Contacto -------------------------------------------------------------------------------- -->
            <div style="display: none;" class="contenedor__formulario formulario--4_filas max-width-100" name="contenedorDatos" id="contenedorContacto">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Detalle Proveedor</h1>
                    </div>
                    <div>
                        <h1>(Contactos)</h1>
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
                </div>
                
                <div class="contenedor__tabla">
                    <table class="tabla" id="tablaContactos" data-rowselected = "-1">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Puesto</th>
                                <th>Telefono</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- El contenido se cargar desde javaScript -->
                        </tbody>
                    </table>
                </div>

                <div class="contenedor__formulario--footer">
                    
                    <div class="form__btn_circle">
                        <button name="btnProveedor" title="Ver Proveedor"><i class="las la-store" ></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button name="btnDireccion" title="Ver Direccion"><i class="las la-map-marker-alt"></i></button>
                    </div>
                    
                    <div class="form__btn_circle margin-right-auto">
                        <button name="btnContactos" title="Ver Contactos" disabled><i class="las la-id-card"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button name="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (contacto)-->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>


<script src="App/js/proveedor/proveedorDetalle.js?v=<%=AppSettings.APP_VERSION_JS%>" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>
