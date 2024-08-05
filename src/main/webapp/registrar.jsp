<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="App/web/shared/head.jsp" >
    <jsp:param name="titleweb" value="Registrar" />
</jsp:include>

<link rel="stylesheet" href="App/css/registrar.css">

<div class="contenedor__general--registrar">
    <div class="contenedor__formulario max-width-80">

        <div class="contenedor__formulario--cabecera">
            <div>
                <h1>Registrar Nuevo Usuario</h1>
            </div>
        </div>

        <div class="contenedor__formulario--main">
            <form class="formulario" name="frmRegistrarUsuario" id="frmRegistrarUsuario">
                <div class="form__input grid-row-span-2">
                    <div>
                        <div class="contenedor__formulario--cabecera--imagen">
                            <img class="imagen--registrar" src="App/img/defaultUser.svg" id="imgUsuario" alt="logo usuario">
                        </div>

                        <label for="btnFoto" class="input_foto">
                            <input type="file" name="btnFoto" id="btnFoto" accept="image/*">
                            <i class="las la-camera"></i>
                            <span id="textoImagen"></span>
                        </label>
                    </div>
                </div>

                <div class="form__input">
                    <input class="" type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Introduce tu nombre de usuario">
                    <label for="nombreUsuario">Usuario</label>
                </div>

                <div class="form__input">
                    <input class="" type="text" name="nombreRealUsuario" id="nombreRealUsuario" placeholder="Introduce tu nombre">
                    <label for="nombreRealUsuario">Nombre</label>
                </div>

                <div class="form__input">
                    <input class="" type="text" name="apellidoRealUsuario" id="apellidoRealUsuario" placeholder="Introduce tus apellidos">
                    <label for="apellidoRealUsuario">Apellidos</label>
                </div>

                <div class="form__input">
                    <input class="" type="text" name="emailUsuario" id="emailUsuario" placeholder="Introduce tu email">
                    <label for="emailUsuario">Email</label>
                </div>

                <div class="form__input">
                    <input class="" type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Introduce tu password">
                    <label for="passwordUsuario">Password</label>
                </div>

                <div class="form__input">
                    <input class="" type="password" name="confirmarPasswordUsuario" id="confirmarPasswordUsuario" placeholder="Introduce tu password">
                    <label for="confirmarPasswordUsuario">Confirmar Password</label>
                </div>

                <input type="hidden" name="imagenUsuarioB64" id="imagenUsuarioB64">
            </form>
        </div>

        <div class="contenedor__formulario--footer">
            <div class="form__btn">
                <button class="form__btn--aceptar" form="frmRegistrarUsuario" type="submit" id="btnRegistrar" disabled>REGISTRAR USUARIO</button>
            </div>
            <div class="form__btn">
                <button class="form__btn--cancelar" id="btnCancelar" id="btnCancelar" disabled>CANCELAR</button>
            </div>
        </div>

    </div> <!-- Fin contenedor__formulario -->
</div>


<script src="App/js/comunes.mjs" type="module"></script>
<script src="App/js/registrar.js" type="module"></script>

<%@ include file="App/web/shared/foot.jsp" %>


