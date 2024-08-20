import { solicitudGet, solicitudPut } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError } from './alertasSweetAlert2.mjs';

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
                    //USER_SELECTED = row.getAttribute("data-id");
                    //redireccion(URL_USER_EDIT + "/" + USER_SELECTED);
                    ActivarUser(element.id, (element.activo) ? 0 : 1);

                });
        }
    }

    solicitudGet("api/identidad/users", adminLoadListUsersCallBack, idElement, true);
}


function ActivarUser(usuarioId, activar) {

    solicitudPut(`api/identidad/activar/${usuarioId}/${activar}`, "", true)
    .then(response => {
        if (response.isError === 1) {
            mostrarMensajeError("No se puede cambiar la activación del usuario", response.result);
        } else {
            mostrarMensaje("Activación Usuario.", `Se han modificado correctamente la activación del usuario`, "success");
        }
    })
    .catch(error => {
        // Maneja el error aquí
        console.error("Error:", error);
        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
    });

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
        //redireccion(URL_USER_EDIT + "/" + USER_SELECTED);
    });
});





export { adminListUsersConfig, adminLoadListUsers };
