import { mostrarMensajeAcceso, mostrarLoading, ocultarLoading } from './alertasSweetAlert2.mjs';

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
 * Realiza una solicitud POST.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {function} callBack  - Método que recibirá el resultado.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
const solicitudPost = (url, callBack, idElement, mostrarLoad) => {
    let data = getDatosForm(idElement);
    solicitudPostFetch(url, data, callBack, idElement, mostrarLoad);
}


/**
 * Realiza una solicitud GET.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {function} callBack  - Método que recibirá el resultado.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
const solicitudGet = (url, callBack, idElement, mostrarLoad) => {
    solicitudGetFetch(url, callBack, idElement, mostrarLoad);
}

/**
 * Realiza una solicitud POST utilizando fetch.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} data        - Los datos a enviar en la solicitud.
 * @param {function} callBack  - Método que recibirá el resultado.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
function solicitudPostFetch(url, data, callBack, idElement, mostrarLoad) {
    formDisable(idElement, true, mostrarLoad);

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
        formDisable(idElement, false, false);
    });
}


/**
 * Realiza una solicitud GET utilizando fetch.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {function} callBack  - Método que recibirá el resultado.
 * @param {string} idElement   - Id del elemento que donde se inyectará los datos.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
function solicitudGetFetch(url, callBack, idElement, mostrarLoad) {

    if (mostrarLoad) {
        mostrarLoading();
    }

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

            if (mostrarLoad) {
                ocultarLoading();
            }
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
 * @param {string}  idForm     - Id del formulario.
 * @param {boolean} disabled   - Estado en el que se quiere poner el formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 * mostrarLoad
 */
function formDisable(idForm, disabled, mostrarLoad) {
    console.log("formDisable : " + disabled);

    if (disabled && mostrarLoad) {
        mostrarLoading();
    }

    $("button, input, select, option, textarea", idForm).prop('disabled', disabled);
}




export { solicitudPost, solicitudGet };