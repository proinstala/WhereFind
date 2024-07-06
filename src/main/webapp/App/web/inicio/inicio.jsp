<%-- 
    Document   : inicio
    Created on : 6 jul 2024, 17:36:35
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link href="../../css/normalize.css" rel="stylesheet" type="text/css"/>
    <link href="../../css/general.css" rel="stylesheet" type="text/css"/>
    
    <link href="../../css/inicio.css" rel="stylesheet" type="text/css"/>
    
    <!-- 
    <link href="../../css/cabecera.css" rel="stylesheet" type="text/css"/>
    -->

    <title>Inicio</title>
</head>
<body>
    <div class="contenedor__general">
        <div class="contenedor">

            <%-- 
            <%@ include file="../cabecera.jsp" %>
            --%>
           <%@ include file="../cabecera/cabecera.jsp" %>
            

            <div class="contenedor__grid--3c">
                <div class="seccion">
                    <p>Almacenes</p>
                </div>
                <div class="seccion">
                    <p>Articulos</p>
                </div>
                <div class="seccion">
                    <p>Otros</p>
                </div>
                
            </div>

            

            <div class="barra__inferior">
                <p>hola</p>
            </div>

        </div>
    </div>
    
    <script src="js/principal.js"></script>
</body>
</html>
