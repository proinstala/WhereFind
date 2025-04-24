
<%@page import="io.proinstala.wherefind.shared.config.AppSettings"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <base href="<%= request.getContextPath() %>/" >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="App/css/lib/normalize.css?v=<%=AppSettings.APP_VERSION_JS%>">
    <link rel="stylesheet" href="App/css/lib/lineAwesome1.3.0/css/line-awesome.css?v=<%=AppSettings.APP_VERSION_JS%>">
    <link rel="stylesheet" href="App/css/lib/sweetalert2.css?v=<%=AppSettings.APP_VERSION_JS%>" type="text/css"/>
    <link rel="stylesheet" href="App/css/personal-sweetAlert2.css?v=<%=AppSettings.APP_VERSION_JS%>" type="text/css"/>
    <link rel="stylesheet" href="App/css/general.css?v=<%=AppSettings.APP_VERSION_JS%>">

    <title><%= request.getParameter("titleweb")%></title>
</head>
<body>
    <script src="App/js/lib/code.jquery.com_jquery-3.7.1.min.js" type="text/javascript"></script>
    <script src="App/js/lib/jquery.validate.js" type="text/javascript"></script>
    <script src="App/js/lib/sweetalert2.js" type="text/javascript"></script>

    <script src="App/js/comunes.mjs?v=<%=AppSettings.APP_VERSION_JS%>" type="module"></script>
 
    
