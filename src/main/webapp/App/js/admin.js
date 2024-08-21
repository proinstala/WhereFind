import { solicitudGet, solicitudPut } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeAdvertencia } from './alertasSweetAlert2.mjs';

const adminLoadListUsers = (idElement, excludeUser = -1) => {

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
            }
            else {
                row?.classList.add("user-deleted");
            }
        }

        for (const element of response.user){
            // Crear una nueva fila en la tabla
            let row = tableRef.insertRow();

            // Asigna la clase a la fila en caso de ser admin o estar eliminado
            usuarioActivoOrEspecial(row, element);

            // Crear y agregar celdas a la fila
            const cell1 = row.insertCell();
            cell1.textContent = element.id;

            const cell2 = row.insertCell();
            const checkboxDiv = document.createElement('div');
            checkboxDiv.classList.add('checkbox-style');

            const checkboxInput = document.createElement('input');
            checkboxInput.type = 'checkbox';
            checkboxInput.id = `switch-${element.id}`;
            checkboxInput.disabled = true;
            checkboxInput.readOnly = true;

            // Asigna el estado de la activación del usuario
            checkboxInput.checked = element.activo;

            // Crear el label asociado al checkbox
            const checkboxLabel = document.createElement('label');
            checkboxLabel.setAttribute('for', `switch-${element.id}`);

            // Construir la celda
            checkboxDiv.appendChild(checkboxInput);
            checkboxDiv.appendChild(checkboxLabel);
            cell2.appendChild(checkboxDiv);

            const cell3 = row.insertCell();
            cell3.textContent = element.userName;

            const cell4 = row.insertCell();
            cell4.textContent = element.nombre;

            const cell5 = row.insertCell();
            cell5.textContent = element.apellidos;

            const cell6 = row.insertCell();
            cell6.textContent = element.rol;

            // Evento para cambiar el estado del checkbox
            checkboxLabel.addEventListener('click', function(event) {
                event.preventDefault(); // Evitar el comportamiento predeterminado del label

                if (excludeUser != -1 && excludeUser === element.id) {
                    mostrarMensajeAdvertencia("Acción no permitida", "No se puede activar/desactivar a si mismo.");
                    return;
                }

                let nuevoEstado = !checkboxInput.checked; // Invertir el estado del checkbox
                // Intenta cambiar el estado de activación del usuario
                activarUser(element.id, nuevoEstado ? 1 : 0).then(response => {
                    if (response) {
                        checkboxInput.checked = nuevoEstado; // Invertir el estado del checkbox
                    }
                })
            });

            // Evento para detectar click en la fila y marcar la fila selecionada
            row.addEventListener('click', () => {
                USER_SELECTED = element.id;
                resetSelected();
                row.classList.add("selected");
            });
        }
    }

    solicitudGet("api/identidad/users", adminLoadListUsersCallBack, idElement, true);
}


function activarUser(usuarioId, activar) {
    return solicitudPut(`api/identidad/activar/${usuarioId}/${activar}`, "", true)
    .then(response => {
        if (response.isError === 1) {
            mostrarMensajeError("No se puede cambiar la activación del usuario", response.result);
        } else {
            mostrarMensaje("Activación Usuario.", `Se han modificado correctamente la activación del usuario`, "success");
        }
        return response.isError != 1;
    })
    .catch(error => {
        // Maneja el error aquí
        console.error("Error:", error);
        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
        return false;
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
