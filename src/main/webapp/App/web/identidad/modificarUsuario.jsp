
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

<jsp:include page="../shared/head.jsp" >
    <jsp:param name="titleweb" value="Index" />
</jsp:include>


<link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../cabecera/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario max-width-100" id="form_usuario">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Modificar Usuario</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos generales del usuario -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarUsuario" id="frmModificarUsuario">
                        <input type="hidden" name="usuario_id" id="usuario_id" value="<%=userDTO.getId()%>">

                        <div class="form__input grid-row-span-2">
                            <div>
                                <div class="contenedor__formulario--cabecera--imagen">
                                    <img class="imagen--registrar" src="<%=userDTO.getImagen()%>" id="imgUsuario" alt="logo usuario">
                                </div>

                                <label for="btnFoto" class="input_foto">
                                    <input type="file" name="btnFoto" id="btnFoto" accept="image/*">
                                    <i class="las la-camera"></i>
                                    <span id="textoImagen"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input class="" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario" value="<%=userDTO.getUserName()%>" disabled readonly>
                            <label for="nombreUsuario">Usuario</label>
                        </div>

                        <div class="form__input disable">
                            <input class="" type="text" name="rolUsuario" id="rolUsuario" placeholder="Introduce tu email" value="<%=userDTO.getRol()%>" readonly>
                            <label for="rolUsuario">Rol</label>
                        </div>

                        <div class="form__input">
                            <input class="" type="text" name="nombreRealUsuario" id="nombreRealUsuario" placeholder="Introduce tu nombre"value="<%=userDTO.getNombre()%>">
                            <label for="nombreRealUsuario">Nombre</label>
                        </div>

                        <div class="form__input">
                            <input class="" type="text" name="apellidoRealUsuario" id="apellidoRealUsuario" placeholder="Introduce tus apellidos"value="<%=userDTO.getApellidos()%>">
                            <label for="apellidoRealUsuario">Apellidos</label>
                        </div>

                        <div class="form__input">
                            <input class="" type="text" name="emailUsuario" id="emailUsuario" placeholder="Introduce tu email" value="<%=userDTO.getEmail()%>">
                            <label for="emailUsuario">Email</label>
                        </div>
                            
                        <input type="hidden" name="imagenUsuarioB64" id="imagenUsuarioB64" value="<%=userDTO.getImagen()%>">

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle centrar_izquierda">
                        <button class="" id="btnPassword" title="Modificar Password"><i class="las la-key"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button class="" form="frmModificarUsuario" id="btnGuardar" title="Guardar" type="submit"><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button class="" id="btnCancelar" title="Cancelar"><i class="las la-times"></i></button>
                    </div>


                    <!-- estos botones esta provisional solo para ver como quedan.
                    <div class="form__btn btnPassword">
                        <button class="form__btn--aceptar" id="btnPassword">MODIFICAR PASSWORD</button>
                    </div>
                    <div class="form__btn">
                        <button class="form__btn--aceptar" form="frmModificarUsuario" type="submit">ACEPTAR</button>
                    </div>
                    <div class="form__btn">
                        <button class="form__btn--cancelar" id="btnCancelar">CANCELAR</button>
                    </div>
                    -->
                </div>

            </div> <!-- Fin contenedor__formulario (usuario)-->
            
            <!-- Formulario para modificar el password de usuario -->
            <div style="display: none;" class="contenedor__formulario max-width-100" id="form_password">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Modificar Password Usuario</h1>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarPassword" id="frmModificarPassword">
                        <input type="hidden" name="usuario_id" id="passwordUsuario_id" value="<%=userDTO.getId()%>">

                        <div class="form__input">
                            <input class="" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password actual">
                            <label for="passwordUsuario">Password Actual</label>
                        </div>
                        
                        <div class="form__input">
                            <input class="" type="password" name="nuevoPassword" id="nuevoPassword" placeholder="Introduce el nuevo password">
                            <label for="passwordUsuario">Nuevo Password</label>
                        </div>
                        
                        <div class="form__input">
                            <input class="" type="password" name="confirmPassword" id="confirmPassword" placeholder="confirma el password">
                            <label for="passwordUsuario">Confirma Password</label>
                        </div>

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle centrar_izquierda">
                        <button class="" id="btnUsuario" title="Modificar Usuario"><i class="las la-user-edit"></i></i></button>
                    </div>
                    
                    <div class="form__btn_circle">
                        <button class="" form="frmModificarPassword" id="btnGuardarPassword" title="Guardar" type="submit"><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button class="" id="btnCancelarPassword" title="Cancelar"><i class="las la-times"></i></button>
                    </div>
                </div>
                        
            </div> <!-- Fin contenedor__formulario (password)-->
   
        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>

<script src="App/js/modificarUsuario.js" type="module" defer></script>

<%@ include file="../shared/foot.jsp" %>