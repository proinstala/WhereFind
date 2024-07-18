import { mostrarMensajeAcceso, mostrarLoading } from './alertasSweetAlert2.mjs';

/**
 * Obtiene los datos del formulario.
 * @param {string}  idForm   - Id del formulario.
 *
 * @returns {string} - Los datos del formulario serializados.
 */
function getDatosForm (idForm) {
    const formData = $(idForm).serialize();
    return formData;
}

/**
 * Realiza una solicitud Post.
 *
 * @param {string} url    - La URL a la que se enviará la solicitud.
  * @param {string} idForm - Id del formulario.
 */
const solicitudPost = (url, idForm) => {
    let data = getDatosForm(idForm);
    solicitudPostFetch(url, data, idForm);
}

const solicitudGet = (url, callBack, idElement) => {
    solicitudGetFetch(url, callBack, idElement);
}

/**
 * Realiza una solicitud POST utilizando fetch.
 *
 * @param {string} url    - La URL a la que se enviará la solicitud.
 * @param {string} data   - Los datos a enviar en la solicitud.
 * @param {string} idForm - Id del formulario.
 */
function solicitudPostFetch(url, data, idForm) {
    formDisable(idForm, true);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(response => {
        if (response.isError === 1) {
            mostrarMensajeAcceso("Acceso Denegado", response.result, "error");
        } else {
            const acceso = () => window.location.replace(response.result);
            mostrarMensajeAcceso(`Bienvenido ${response.user.nombre}`, "Acceso Permitido.", "success", (response.isUrl)? acceso : null);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        mostrarMensajeAcceso("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
    })
    .finally(() => {
        console.log("complete");
        formDisable(idForm, false);
    });
}


/**
 * Realiza una solicitud GET utilizando fetch.
 *
 * @param {string} url    - La URL a la que se enviará la solicitud.
 */
function solicitudGetFetch(url, callBack, idElement) {
    fetch(url, {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
        .then(response => {
            callBack(response, idElement);
    })
    .catch(error => {
        console.error("Error:", error);
        mostrarMensajeAcceso("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
    })
    .finally(() => {
        console.log("complete");
        //formDisable(idForm, false);
    });
}




/**
 * Activa/desactiva todos los campos de un formulario y muestra la alerta de cargando.
 *
 * @param {string}  idForm   - Id del formulario.
 * @param {boolean} disabled - Estado en el que se quiere poner el formulario.
 */
function formDisable(idForm, disabled) {
    console.log("formDisable : " + disabled);

    if (disabled) {
        mostrarLoading();
    }

    $("button, input, select, option, textarea", idForm).prop('disabled', disabled);
}




export { solicitudPost, solicitudGet };