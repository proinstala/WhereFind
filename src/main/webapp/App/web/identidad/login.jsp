
<%@page import="io.proinstala.wherefind.shared.consts.urls.UrlsInternas"%>
<%@page import="io.proinstala.wherefind.shared.consts.urls.enums.UrlIdentidad"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Login" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">

    <div class="contenedor__general--login">
        <div class="login">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmLogin" id="frmLogin" >
                <h1>Login</h1>
                <img class="imagen--login" src="App/img/defaultUser.svg" alt="logo usuario">

                <div class="login__input">
                    <input class="input-underline" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario">
                    <label for="nombreUsuario">Usuario</label>
                </div>
                <div class="login__input">
                    <input class="input-underline" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password">
                    <label for="passwordUsuario">Password Usuario</label>
                </div>
                <div class="login__input">
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
                    <a class="login__enlaces-enlace" href="<%=UrlsInternas.getIdentidadUri(UrlIdentidad.REGISTRAR)%>">Crear usuario</a>
                    <a class="login__enlaces-enlace" href="<%=UrlsInternas.getIdentidadUri(UrlIdentidad.RECOVERY)%>">He olvidado mi password</a>
                </div>
            </div>

        </div>
    </div>

    <script src="App/js/comunes.mjs" type="module"></script>
    <script src="App/js/login.js" type="module"></script>

<%@ include file="/App/web/shared/foot.jsp" %>
