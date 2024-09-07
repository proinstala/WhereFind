<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="io.proinstala.wherefind.shared.dtos.CardDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="cards">

<%

    List<CardDTO> cards = (ArrayList<CardDTO>)request.getAttribute("cards");
    if (cards != null)
    {
        for (CardDTO tarjeta : cards)
        {
%>
                <a class="card card__link-main" href="<%= tarjeta.url() %>">
                    <h2 class="card__title"><i class="circle <%= tarjeta.classIcon()%>"></i> <%= tarjeta.titulo()%></h2>
                    <p class="card__descripcion"><%= tarjeta.descripcion() %></p>
                    <div class="card__apply"><span class="card__link">Ir ahora <i class="las la-arrow-right"></i></span></div>
                </a>
<%
        }
    }
%>

</div>