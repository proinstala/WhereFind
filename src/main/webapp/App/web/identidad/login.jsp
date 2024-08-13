<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Login" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">
    <link href="App/css/formulario.css" rel="stylesheet" type="text/css"/>

    <div class="contenedor__general--login">
        <div class="login">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmLogin" id="frmLogin" >
                <h1>Login</h1>
                <img src="App/img/defaultUser.svg" alt="logo usuario">

                <div class="form__input login__input">
                    <input class="input-underline" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario">
                    <label for="nombreUsuario">Usuario</label>
                </div>
                <div class="form__input login__input">
                    <input class="input-underline" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password">
                    <label for="passwordUsuario">Password Usuario</label>
                </div>
                <div class="form__input login__input">
                    <button id="btnEntrar" type="submit" disabled>ENTRAR</button>
                </div>
            </form>

            <!-- Parte derecha -->
            <div class="login__derecha">
                <h1>Bienvenido<br>a<br>WhereFind</h1>
                <p>Organiza, cataloga y encuentra tus artí­culos de manera rápida y eficiente.</p>
                <div class="login__logo">
                    <img src="App/img/caja.png" alt="caja carton">
                </div>

                <div class="login__enlaces">
                    <a class="login__enlaces-enlace" href="<%=UrlIdentidad.REGISTRAR.getUri()%>">Crear usuario</a>
                    <a class="login__enlaces-enlace" href="<%=UrlIdentidad.RECOVERY.getUri()%>">He olvidado mi password</a>
                </div>
            </div>

        </div>
    </div>

    <script src="App/js/comunes.mjs" type="module"></script>
    <script src="App/js/login.js" type="module"></script>

<%@ include file="/App/web/shared/foot.jsp" %>
