<%@page contentType="text/html" pageEncoding="UTF-8"%>



<div class="card">
    <h2 class="card__title"><i class="circle <%= request.getParameter("iconCard")%>"></i> <%= request.getParameter("titleCard")%></h2>
    <p class="card__descripcion">
        <%= request.getParameter("descriptionCard")%>
    </p>
    <p class="card__apply">
        <a class="card__link" href="<%= request.getParameter("urlCard")%>">Ir ahora <i class="las la-arrow-right"></i></a>
    </p>
</div>
