<%--
    Document   : cabecera
    Created on : 6 jul 2024, 17:49:25
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<link href="App/css/cabecera.css" rel="stylesheet" type="text/css"/>

<div class="cabecera">
        <div class="btn__configuracion">
            <img class="btn__configuracion--imagen" src="App/img/gear_white.svg" alt="icono configuración">
        </div>
        <div class="contenedor__menu">
            <a href="index.jsp">Index</a>
        </div>
        <div class="contenedor__usuario">
            <div class="imagen__usuario">
                <img class="imagen__usuario--imagen" src="App/img/indiana_jones.jpeg" alt="foto usuario">
            </div>
            <button class="btn_prueba">1</button>
            <button class="btn_prueba">config</button>
            <a href="api/identidad/logout"><button class="btn_prueba">cerrar</button></a>
        </div>
</div>
