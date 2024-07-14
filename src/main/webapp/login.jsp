<%--
    Document   : index
    Created on : 5 jul 2024, 21:09:53
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Login" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">

    <div class="contenedor__general--login">
        <div class="login">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmLogin" id="frmLogin" >
                <h1 class="login__titulo">Login</h1>
                <img class="login__img" src="App/img/user2.svg" alt="logo usuario">

                <div class="info-error" id="info-error">
                    <b>Errores</b>
                </div>

                <div class="login__input">
                    <label for="nombreUsuario">Usuario</label>
                    <input class="input-underline" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario">
                </div>
                <div class="login__input">
                    <label for="passwordUsuario">Password</label>
                    <input class="input-underline" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password">
                </div>
                <div class="login__input">
                    <button class="login__btn" type="submit">ENTRAR</button>
                </div>
            </form>

            <!-- Parte derecha -->
            <div class="login__derecha">
                <h1 class="login__titulo-saludo">Bienvenido a WhereFind</h1>
                <p class="login__descripcion">Organiza, cataloga y encuentra tus artí­culos de manera rápida y eficiente.</p>
                <div class="login__imagen">
                    <img src="App/img/caja.png" alt="caja carton">
                </div>

                <div class="login__enlaces">
                    <a class="login__enlaces-enlace" href="#">Crear usuario</a>
                    <a class="login__enlaces-enlace" href="#">He olvidado mi password</a>
                </div>
            </div>

        </div>
    </div>

    
    <!-- comment <script src="App/js/login.js" type="text/javascript"></script> -->
    <script src="App/js/login.js" type="module"></script>

<%@ include file="App/web/shared/foot.jsp" %>
