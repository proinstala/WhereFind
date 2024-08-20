import { solicitudGet } from './comunes.mjs';

const adminLoadListUsers = (idElement) => {

    const adminLoadListUsersCallBack = (response, idElement)  => {

        let tableRef = document.getElementById(idElement).getElementsByTagName('tbody')[0];

        function resetSelected() {
            for (const row of tableRef.rows){

                if (row) {
                    row.classList.remove("selected");
                }
            }
        }

        function usuarioActivoOrEspecial(row, user) {

            if (user.activo) {

                if (user.rol == "Admin") {
                    row?.classList.add("user-admin");
                }

                return "checked"
            }
            else {
                row?.classList.add("user-deleted");
            }
        }



        for (const element of response.user){
            let row = tableRef.insertRow();
            row.setAttribute("data-id", element.id);
            row.innerHTML =
                "<td>" + element.id + "</td>" +
                "<td><div div class='checkbox-style'><input type='checkbox' "+usuarioActivoOrEspecial(row, element)+" force-disabed=true disabled readonly id='switch-" + element.id + "' /> <label for='switch-" + element.id + "'></label></div></td> " +
                "<td>" + element.userName + "</td>"+
                "<td>" +element.nombre+ "</td>"+
                "<td>" +element.apellidos+ "</td>"+
                "<td>" + element.rol + "</td>";

                row.addEventListener('click', () => {
                    USER_SELECTED = row.getAttribute("data-id");
                    resetSelected();
                    row.classList.add("selected");
                });

                row.addEventListener('dblclick', () => {
                    USER_SELECTED = row.getAttribute("data-id");
                    redireccion(URL_USER_EDIT + "/" + USER_SELECTED);
                });
        }
    }

    solicitudGet("api/identidad/users", adminLoadListUsersCallBack, idElement, true);
}


let URL_USER_EDIT;
let USER_SELECTED;

const adminListUsersConfig = (urlUserEditar) => {
    URL_USER_EDIT = urlUserEditar;
}



function redireccion(url) {
    window.location.href = url;
}


$(document).ready(function () {
    const btnModificar = document.querySelector('#btnModificar');
    btnModificar.addEventListener('click', () => {
        redireccion(URL_USER_EDIT + "/" + USER_SELECTED);
    });
});





export { adminListUsersConfig, adminLoadListUsers };
