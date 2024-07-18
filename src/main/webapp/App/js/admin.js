import { solicitudGet } from './comunes.mjs';

const adminLoadListUsers = (idElement) => {

    const adminLoadListUsersCallBack = (response, idElement)  => {

        let tableRef = document.getElementById(idElement).getElementsByTagName('tbody')[0];

        for (const element of response.user){
            tableRef.insertRow().innerHTML =
                "<td>" + element.id + "</td>" +
                "<td>" + element.userName + "</td>"+
                "<td>" +element.nombre+ "</td>"+
                "<td>" +element.apellidos+ "</td>"+
                "<td>" +element.rol+ "</td>";
        }
    }

    solicitudGet("api/identidad/users", adminLoadListUsersCallBack, idElement);
}


export { adminLoadListUsers };
