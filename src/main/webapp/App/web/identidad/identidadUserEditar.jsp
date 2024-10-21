
<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlApp"%>
<%@page import="io.proinstala.wherefind.shared.dtos.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page import="io.proinstala.wherefind.api.identidad.UserSession"%>

<%!
    private String forceDisabled(HttpServletRequest request, UserDTO userDTO)
    {
        // Desactiva los campos si, está editando un usuario normal o lo que se está editando es su propio usuario como admin.
        if (request.getAttribute("userIdByAdmin") == null || userDTO.getId() == UserSession.getUserLogin(request).getId())
        {
            // Código si el atributo existe
            return " force-disabed=true disabled readonly ";
        }

        return "";
    }

    private String getSelectValue(String rolUsuario, String rolUsuarioPorDefecto )
    {
        String resultado = "value='" + rolUsuario + "'";
        if (rolUsuario.equals(rolUsuarioPorDefecto))
        {
            resultado += " selected";
        }

        return resultado;
    }

    private String getUrlCancelar(boolean isEditByAdmin)
    {
        if (isEditByAdmin)
        {
            return UrlAdmin.USER_LISTA.getUri();
        }
        else
        {
            return UrlApp.HOME.getUri();
        }
    }
%>

<%
    // Si no se está logueado se manda al usuario al login.jsp
    if(UserSession.redireccionarIsUserNotLogIn(new ActionServer(request, response))){
        // Detiene la ejecución de este servlet
        return;
    }

    // Se define la variable para determinar si es un admin el que esta editando los datos del usuario
    boolean isEditByAdmin = false;

    UserDTO userDTO;
    if (request.getAttribute("userIdByAdmin") != null) {
        // Código si el atributo existe
        userDTO = new UserDTO();

        userDTO.setId((int)request.getAttribute("userIdByAdmin"));
        userDTO.setUserName("");
        userDTO.setPassword("");
        userDTO.setRol("");
        userDTO.setNombre("");
        userDTO.setApellidos("");
        userDTO.setEmail("");
        userDTO.setImagen("");
        userDTO.setActivo(true);

        // Guarda el id del usuario que esta editando el administrador en su sesión
        UserSession.setSessionValue(request, "idUserEditByAdmin", userDTO.getId());

        // Indicamos que sí, es un admin editando al usuario
        isEditByAdmin = true;
    }
    else
    {
        userDTO = UserSession.getUserLogin(request);
    }
%>

<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Editar Usuario" />
</jsp:include>

<link href="App/css/formulario.css?v=20241021_184300" rel="stylesheet" type="text/css"/>

<div class="contenedor__general">
    <div class="contenedor">

        <%@ include file="../shared/cabecera.jsp" %>

        <div class="main">

            <div class="contenedor__formulario formulario--3_filas max-width-100" id="form_usuario">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Editar Usuario</h1>
                    </div>
                </div>

                <!-- Formulario para modificar los datos generales del usuario -->
                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarUsuario" id="frmModificarUsuario">
                        <input type="hidden" name="usuario_id" id="usuario_id" value="<%=userDTO.getId()%>">
<%
                    // ----------------------------------------
                    // Si es un admin el que edita al usuario
                    // ----------------------------------------
                    if (isEditByAdmin)
                    {
%>
                        <input type="hidden" name="usuario_need_load" id="usuario_need_load" value="1">
<%
                    }
                    // ----------------------------------------
                    // Si es un admin el que edita al usuario
                    // ----------------------------------------
%>

                        <div class="form__input grid-row-span-2">
                            <div>
                                <div class="contenedor__formulario--imagen--redondo">
                                    <img src="<%=userDTO.getImagen()%>" id="imgUsuario" alt="logo usuario">
                                </div>

                                <label for="btnFoto" class="input_foto">
                                    <input type="file" name="btnFoto" id="btnFoto" accept="image/*">
                                    <i class="las la-camera"></i>
                                    <span id="textoImagen"></span>
                                </label>
                            </div>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario" value="<%=userDTO.getUserName()%>" <%= forceDisabled(request, userDTO) %>>
                            <label for="nombreUsuario">Usuario</label>
                        </div>

                        <div class="form__input">
                            <select name="rolUsuario" id="rolUsuario" placeholder="Introduce el rol del usuario" <%= forceDisabled(request, userDTO) %>>
                                <option <%= getSelectValue("Admin", userDTO.getRol()) %>>Admin</option>
                                <option <%= getSelectValue("User", userDTO.getRol()) %>>User</option>
                            </select>
                            <label for="rolUsuario">Rol</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="nombreRealUsuario" id="nombreRealUsuario" placeholder="Introduce tu nombre"value="<%=userDTO.getNombre()%>">
                            <label for="nombreRealUsuario">Nombre</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="apellidoRealUsuario" id="apellidoRealUsuario" placeholder="Introduce tus apellidos"value="<%=userDTO.getApellidos()%>">
                            <label for="apellidoRealUsuario">Apellidos</label>
                        </div>

                        <div class="form__input">
                            <input type="text" name="emailUsuario" id="emailUsuario" placeholder="Introduce tu email" value="<%=userDTO.getEmail()%>">
                            <label for="emailUsuario">Email</label>
                        </div>

                        <input type="hidden" name="imagenUsuarioB64" id="imagenUsuarioB64" value="<%=userDTO.getImagen()%>">

                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle margin-right-auto">
                        <button id="btnPassword" title="Modificar Password"><i class="las la-key"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button form="frmModificarUsuario" id="btnGuardar" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosUsuario" title="Deshacer cambios" disabled><i class="las la-redo-alt"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnCancelar" title="Cancelar" data-uri="<%= getUrlCancelar(isEditByAdmin) %>"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (usuario)-->

            <!-- Formulario para modificar el password de usuario -->
            <div style="display: none;" class="contenedor__formulario formulario--3_filas max-width-100" id="form_password">

                <div class="contenedor__formulario--cabecera">
                    <div>
                        <h1>Modificar Password Usuario</h1>
                    </div>
                </div>

                <div class="contenedor__formulario--main">
                    <form class="formulario" name="frmModificarPassword" id="frmModificarPassword">
                        <input type="hidden" name="usuario_id" id="passwordUsuario_id" value="<%=userDTO.getId()%>">

<%
                    // ----------------------------------------
                    // Si no es un admin el que edita al usuario
                    // ----------------------------------------
                    if (!isEditByAdmin)
                    {
%>
                        <div class="form__input">
                            <input type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password actual">
                            <label for="passwordUsuario">Password Actual</label>
                        </div>
<%
                    }
                    // ----------------------------------------
                    // Si no es un admin el que edita al usuario
                    // ----------------------------------------
%>
                        <div class="form__input">
                            <input type="password" name="nuevoPassword" id="nuevoPassword" placeholder="Introduce el nuevo password">
                            <label for="nuevoPassword">Nuevo Password</label>
                        </div>

                        <div class="form__input">
                            <input type="password" name="confirmPassword" id="confirmPassword" placeholder="confirma el password">
                            <label for="confirmPassword">Confirma Password</label>
                        </div>
                    </form>
                </div>

                <div class="contenedor__formulario--footer">
                    <div class="form__btn_circle margin-right-auto">
                        <button id="btnUsuario" title="Modificar Usuario"><i class="las la-user-edit"></i></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button form="frmModificarPassword" id="btnGuardarPassword" title="Guardar" type="submit" disabled><i class="las la-save"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnDeshacerCambiosPassword" title="Deshacer cambios" disabled><i class="las la-redo-alt"></i></button>
                    </div>

                    <div class="form__btn_circle">
                        <button id="btnCancelarPassword" title="Cancelar" data-uri="<%= getUrlCancelar(isEditByAdmin) %>"><i class="las la-times"></i></button>
                    </div>
                </div>

            </div> <!-- Fin contenedor__formulario (password)-->

        </div> <!-- Fin main -->

        <div class="barra__inferior">
            <p>WhereFind 1.0</p>
        </div>

    </div>
</div>

<script src="App/js/identidad/modificarUsuario.js?v=20241021_184300" type="module" defer></script>

<%@ include file="/App/web/shared/foot.jsp" %>