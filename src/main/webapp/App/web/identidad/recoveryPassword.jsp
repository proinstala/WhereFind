
<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="He olvidado mi password" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">
    <link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>

    <div class="contenedor__general--login">
        <div class="login">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmRecovery" id="frmRecovery" >
                <h4>He olvidado mi password</h4>
                <i class="las la-user-check imagen-recovery"></i>

                <div class="login__input">
                    <input class="input-underline" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario o el email">
                    <label for="nombreUsuario">Usuario o Email</label>
                </div>
                <div class="login__input">
                    <button id="btnEntrar" type="submit" disabled>RECUPERAR</button>
                </div>
            </form>

            <!-- Parte derecha -->
            <div class="login__derecha">
                <div>
                    <h4>Recuperación de Contraseña Olvidada</h4>
                    <p>¿Olvidaste tu contraseña? Solo necesitas proporcionar tu <strong>nombre de usuario</strong> o <strong>correo electrónico</strong> asociado. Sigue estos pasos para restablecer tu contraseña:</p>
                    <ol>
                        <li>Introduce tu <strong>nombre de usuario</strong> o <strong>correo electrónico</strong> en el campo proporcionado.</li>
                        <li>Haz clic en el botón <strong>"RECUPERAR"</strong>.</li>
                        <li>Si los datos ingresados coinciden con una cuenta existente, te enviaremos un correo electrónico con las instrucciones para restablecer tu contraseña.</li>
                        <li>Revisa tu bandeja de entrada (incluyendo la carpeta de spam) para encontrar un email nuestro con el enlace de recuperación.</li>
                        <li>Haz clic en el enlace del correo para crear una nueva contraseña y acceder nuevamente a tu cuenta.</li>
                    </ol>
                    <p>Si tienes algún problema durante el proceso, no dudes en contactar con nuestro equipo de soporte.</p>
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
