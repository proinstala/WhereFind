<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Cambiar password" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">
    <link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>


    <div class="contenedor__general--login">
        <div class="login">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmRecoveryFinal" id="frmRecoveryFinal" >
                <input type="hidden" name="usuario_id" id="passwordUsuario_id" value="${userDTO.getId()}">
                <input type="hidden" name="reset" value="1">

                <h3>Cambiar password</h3>
                <i class="las la-key imagen-recovery"></i>

                <div class="form__input">
                    <input class="" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce el nuevo password">
                    <label for="passwordUsuario">Nueva Contraseña</label>
                </div>

                <div class="form__input">
                    <input class="" type="password" name="nuevoPassword" id="nuevoPassword" placeholder="Vuelve a introducir el nuevo password">
                    <label for="passwordUsuario">Verificar Contraseña</label>
                </div>


                <div class="login__input">
                    <button id="btnEntrar" type="submit" disabled>CAMBIAR</button>
                </div>
            </form>

            <!-- Parte derecha -->
            <div class="login__derecha">
                <div>
                    <h4>Restablecer Contraseña</h4>
                    <p>Por favor, ingrese su nueva contraseña en los campos a continuación para completar el proceso de recuperación. Asegúrate de que tu nueva contraseña sea segura y fácil de recordar.</p>
                    <ul>
                        <li><strong>Nueva Contraseña:</strong> Introduce tu nueva contraseña.</li>
                        <li><strong>Verificar Contraseña:</strong> Vuelve a introducir la nueva contraseña para confirmarla.</li>
                    </ul>
                    <p>Una vez que hayas completado ambos campos, haz clic en el botón <strong>"CAMBIAR"</strong> para guardar los cambios y acceder a tu cuenta.</p>
                    <p>Si tienes algún problema, no dudes en contactar con nuestro equipo de soporte.</p>
                </div>


                <div class="login__enlaces">
                    <a class="login__enlaces-enlace" href="<%=UrlIdentidad.REGISTRAR.getUri()%>">Crear usuario</a>
                    <a class="login__enlaces-enlace" href="<%=UrlIdentidad.LOGIN.getUri()%>">Login</a>
                </div>
            </div>

        </div>
    </div>

    <script src="App/js/comunes.mjs" type="module"></script>
    <script src="App/js/recovery.js" type="module"></script>

<%@ include file="/App/web/shared/foot.jsp" %>



