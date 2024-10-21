import { solicitudGet, solicitudPut } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeAdvertencia } from '../alertasSweetAlert2.mjs?v=20241021_184300';

// Configuración de las urls
const URL_USER_EDIT = "admin/users/edit";

// Usuario seleccionado
let USER_SELECTED;


$(document).ready(function () {
    const btnModificar = document.querySelector('#btnModificar');
    btnModificar.addEventListener('click', () => {
        redireccion(URL_USER_EDIT + "/" + USER_SELECTED);
    });

    const buscarUsuario = document.querySelector('#buscarUsuario');
    buscarUsuario?.addEventListener('keyup', () => {

            // Obtener el valor del input y convertirlo a minúsculas
            const filter = buscarUsuario.value.toLowerCase();

            // Obtener todas las filas del cuerpo de la tabla
            const table = document.getElementById('admin-list-users');
            const tr = table.getElementsByTagName('tr');

            // Recorrer todas las filas de la tabla (excepto el encabezado)
            for (let i = 1; i < tr.length; i++) {
                let row = tr[i];
                let textContent = row.textContent || row.innerText;

                // Verificar si la fila contiene el texto del input
                if (textContent.toLowerCase().indexOf(filter) > -1) {
                    row.style.display = ''; // Mostrar la fila
                } else {
                    row.style.display = 'none'; // Ocultar la fila
                }
            }

    });


    onSeleccionarUserFromList(false);
});

function onSeleccionarUserFromList(seleccionado) {
    $("#btnModificar").prop('disabled', !seleccionado);
}


/**
 * Carga una lista de usuarios excluyendo un usuario específico de la opción de activación/desactivación.
 * @param {string} idElement El ID del elemento donde se mostrará la información de los usuarios cargados.
 * @param {number} [excludeUser=-1] El número de identificación (ID) del usuario a excluir, por defecto es -1.
 */
const adminLoadListUsers = (idElement, excludeUser = -1) => {

    const adminLoadListUsersCallBack = (response)  => {

        let tableRef = document.getElementById(idElement).getElementsByTagName('tbody')[0];

        /**
         * Esta función reinicia los elementos seleccionados previamente en una interfaz de usuario.
         */
        function resetSelected() {
            for (const row of tableRef.rows){

                if (row) {
                    row.classList.remove("selected");
                }
            }
        }

        /**
         * Añade una clase CSS a la fila de un usuario según su estado y rol en el sistema.
         * Si el usuario está activo, añadirá la clase "user-admin" si es administrador o no añadirá nada si no lo es.
         * En cambio, si el usuario NO está activo, añadirá la clase "user-deleted".
         * @param {HTMLElement} row - Elemento HTML donde se agregará la clase CSS correspondiente.
         * @param {Object} user - Objeto que contiene información del usuario, como su estado (activo/no activo) y rol ("Admin" o cualquier otro).
         */
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

        for (const element of response.data){
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

                // Indicar que se ha seleccionado un usuario
                onSeleccionarUserFromList(true);
            });
        }
    }

    return solicitudGet("api/identidad/users", idElement, true)
        .then(response => {
        if (response.isError === 1) {
            mostrarMensajeError("Se ha producido un error", response.result);
        } else {
            adminLoadListUsersCallBack(response);
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


/**
 * Esta función activa o desactiva a un usuario de un sistema, según los parámetros proporcionados.
 * Realiza una solicitud PUT a la API y gestiona la respuesta para mostrar mensajes al usuario en caso de exito o error.
 * @param {number} usuarioId - El identificador numérico del usuario en el sistema.
 * @param {int} activar - Un entero 0 o 1 que indica si se desea activar (true) o desactivar (false) al usuario.
 * @return {Promise<boolean>} Una promesa que se resuelve a false en caso de error y true en caso contrario.
 */
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


/**
 * Redirige a la dirección URL proporcionada por el usuario seguro usando HTTPS (si está disponible).
 * Convierte una cadena de URL no segura en segura, si es posible.
 * @param {string} url - La dirección URL para redireccionar al usuario a. Por favor, proporcione la dirección completa con http o https.
 */
function redireccion(url) {
    window.location.href = url;
}





export { adminLoadListUsers };
