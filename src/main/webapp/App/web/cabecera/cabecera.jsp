<%--
    Document   : cabecera
    Created on : 6 jul 2024, 17:49:25
    Author     : David
--%>

<%@page import="io.proinstala.wherefind.shared.controllers.actions.ActionServer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="io.proinstala.wherefind.identidad.UserSession"%>

<link href="App/css/cabecera.css" rel="stylesheet" type="text/css"/>

<div class="cabecera">
    <div class="contenedor__inicio" title="Configuración">
        <!--
            <img class="contenedor__inicio--imagen" src="App/img/gear_white.svg" alt="icono configuración">
        -->
        <svg class="contenedor__inicio--config" viewBox="0 0 32 32" xml:space="preserve" xmlns="http://www.w3.org/2000/svg">
            <path id="svg-gear" d="M27.526 18.036 27 17.732c-.626-.361-1-1.009-1-1.732s.374-1.371 1-1.732l.526-.304a2.999 2.999 0 0 0 1.098-4.098l-1-1.732a3.003 3.003 0 0 0-4.098-1.098L23 7.339a1.977 1.977 0 0 1-2 0 1.98 1.98 0 0 1-1-1.732V5c0-1.654-1.346-3-3-3h-2c-1.654 0-3 1.346-3 3v.608a1.98 1.98 0 0 1-1 1.732 1.98 1.98 0 0 1-2 0l-.526-.304a3.005 3.005 0 0 0-4.099 1.098l-1 1.732a2.998 2.998 0 0 0 1.098 4.098l.527.304c.626.361 1 1.009 1 1.732s-.374 1.371-1 1.732l-.526.304a2.998 2.998 0 0 0-1.098 4.098l1 1.732a3.004 3.004 0 0 0 4.098 1.098L9 24.661a1.977 1.977 0 0 1 2 0 1.98 1.98 0 0 1 1 1.732V27c0 1.654 1.346 3 3 3h2c1.654 0 3-1.346 3-3v-.608c0-.723.374-1.37 1-1.732a1.98 1.98 0 0 1 2 0l.526.304a3.005 3.005 0 0 0 4.098-1.098l1-1.732a2.998 2.998 0 0 0-1.098-4.098zM16 21c-2.757 0-5-2.243-5-5s2.243-5 5-5 5 2.243 5 5-2.243 5-5 5z" fill="#ffffff" ></path>
        </svg>
    </div>

    <div class="contenedor__menu">
        <i class="las la-warehouse" title="Almacen"></i>
        <i class="las la-box" title="Articulo"></i>
        <i class="las la-map-marked-alt" title="Direccion"></i>
        <i class="las la-store-alt" title="Proveedor"></i>
<%
    // ----------------------------------------
    // Iconos que solo muestra al administrador
    // ----------------------------------------
    if (UserSession.isUserLogIn(new ActionServer(request, response), true))
    {
%>

        <a href="admin/users"><i class="las la-users-cog" title="Usuarios"></i></a>

<%
    }
    // ----------------------------------------
    // Iconos que solo muestra al administrador
    // ----------------------------------------
%>


    </div>

    <div class="contenedor__usuario">
        <div class="imagen__usuario">
            <img class="imagen__usuario--imagen" src="App/img/indiana_jones.jpeg" alt="foto usuario">
        </div>

        <div class="info_user">
            <p id="nombreUser"><%= UserSession.getLoginUserFullName(request) %></p>
            <p id="rolUser"><%= UserSession.getLoginUserRol(request) %></p>
        </div>

        <button class="btn_user las la-pen" title="Editar Usuario"></button>

        <!-- muestra
        <i class="las la-pen"></i>
        <i class="las la-user-edit"></i>
        <i class="las la-sign-out-alt"></i>
        <i class="las la-door-closed"></i>
        <i class="las la-microchip"></i>


        - almacen - <i class="las la-warehouse"></i>
	- almacen - <i class="las la-archive"></i>

	- articulo - <i class="las la-box"></i>
	- articulos - <i class="las la-boxes"></i>

	- Ubicacion - <i class="las la-map-marked-alt"></i>
	- Ubicacion - <i class="las la-compass"></i> <i class="lar la-compass"></i>
	- Ubicacion - <i class="las la-map-marker-alt"></i>

	- proveedor - <i class="las la-store-alt"></i>
        -->

        <a href="api/identidad/logout"><button class="btn_user las la-sign-out-alt" title="Cerrar Sesión"></button></a>
    </div>
</div>
