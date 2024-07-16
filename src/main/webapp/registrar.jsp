<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Registrar" />
</jsp:include>

    <link rel="stylesheet" href="App/css/login.css">
    <link rel="stylesheet" href="App/css/registrar.css">

    <div class="contenedor__general--login">
        <div class="login registrar">
            <!-- Parte izquierda -->
            <form class="login__izquierda" name="frmUser" id="frmUser" >
                <h1 class="login__titulo">Registrar</h1>
                <img class="login__img" src="App/img/user2.svg" alt="logo usuario">

                <div class="info-error" id="info-error">
                    <b>Errores</b>
                </div>

                <div class="login__input">
                    <label for="nombreUsuario">Usuario</label>
                    <input class="input-underline" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario">
                </div>

                <div class="login__input">
                    <label for="nombreRealUsuario">Nombre</label>
                    <input class="input-underline" type="text" name="nombreRealUsuario" id="nombreRealUsuario" placeholder="Introduce tu nombre">
                </div>

                <div class="login__input">
                    <label for="apellidoRealUsuario">Apellidos</label>
                    <input class="input-underline" type="text" name="apellidoRealUsuario" id="apellidoRealUsuario" placeholder="Introduce tus apellidos">
                </div>

                <div class="login__input">
                    <label for="emailUsuario">Email</label>
                    <input class="input-underline" type="text" name="emailUsuario" id="emailUsuario" placeholder="Introduce tu email">
                </div>

                <div class="login__input">
                    <label for="passwordUsuario">Password</label>
                    <input class="input-underline" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password">
                </div>

                <div class="login__input">
                    <button class="login__btn" type="submit">REGISTRAR</button>
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
                    <a class="login__enlaces-enlace" href="login.jsp">Login</a>
                    <a class="login__enlaces-enlace" href="#">He olvidado mi password</a>
                </div>
            </div>

        </div>
    </div>


    <script src="App/js/comunes.mjs" type="module"></script>
    <script src="App/js/registrar.js" type="module"></script>

<%@ include file="App/web/shared/foot.jsp" %>


