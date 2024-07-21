import { mostrarMensajeAcceso, mostrarLoading, ocultarLoading, mostrarMensajeAdvertencia } from './alertasSweetAlert2.mjs';

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
        callBack(response);
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


/**
 * Establece una imagen en un contenedor dado a partir de un archivo de imagen seleccionado,
 * actualiza un campo oculto con la representación Base64 de la imagen, y maneja errores 
 * mostrando una advertencia y configurando una imagen por defecto si es necesario.
 * 
 * 
 * 
 * @param {File} fileImage - El archivo de imagen que se va a establecer en el contenedor.
 * @param {HTMLImageElement} contenedorImagen - El contenedor de imagen (etiqueta <img>) donde se mostrará la imagen.
 * @param {HTMLInputElement} inputHideImagen64 - Un campo de entrada oculto donde se almacenará la representación Base64 de la imagen.
 * @param {string} [defaultImage] - La URL de la imagen por defecto que se mostrará si la imagen no es válida. Si no se proporciona, no se cambia la imagen.
 * @param {number} [maxSizeInMB=1] - El tamaño máximo permitido para el archivo de imagen en megabytes. El valor predeterminado es 1 MB.
 * 
 * @returns {Promise<boolean>} - Una promesa que se resuelve en `true` si la imagen es válida y se ha establecido correctamente. La promesa se rechaza con un error si ocurre un problema.
 */
const setImageSelected = (fileImage, contenedorImagen, inputHideImagen64, defaultImage, maxSizeInMB) => {
    return new Promise((resolve, reject) => {
        validateImage(fileImage).then(() => {
            const reader = new FileReader(); //Crea un objeto FileReader para leer el contenido del archivo.
            reader.readAsDataURL(fileImage); //Lee el contenido del archivo como una URL de datos.
            reader.onload = function(e) {
                const base64String = e.target.result;   //Obtén la URL de datos Base64
                contenedorImagen.src = base64String;    //Asigna el contenido del archivo como una URL de datos a la imagen.
                inputHideImagen64.value = base64String; //Establece el valor del input oculto con la representación Base64 de la imagen.
                resolve(true); // Resuelve la promesa en true cuando la imagen se establece correctamente.
            };

        }).catch(error => {
            mostrarMensajeAdvertencia("Imagen no válida", error, "warning");
            if(defaultImage) {
                contenedorImagen.src = defaultImage; //Si no se seleccionó ningún archivo válido, muestra la imagen por defecto.
            }
            reject(error); // Rechaza la promesa en false cuando hay un error.
        });
    });
}


/**
 * Valida si el archivo proporcionado es una imagen válida y si su tamaño está dentro del límite especificado.
 * 
 * @param {File} fileImage - El archivo de imagen a validar.
 * @param {number} [maxSizeInMB=1] - El tamaño máximo permitido para el archivo de imagen en megabytes. El valor predeterminado es 1 MB.
 * 
 * @returns {Promise<boolean>} - Una promesa que se resuelve en `true` si el archivo es una imagen válida y está dentro del límite de tamaño, o se rechaza con un mensaje de error si no lo es.
 */
function validateImage(fileImage, maxSizeInMB = 1) {
    return new Promise((resolve, reject) => {
        //Verifica si el archivo es de tipo imagen.
        const validImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/avif', 'image/webp'];
        if (!validImageTypes.includes(fileImage.type)) {
            reject("El archivo seleccionado no es una imagen o no es una imagen válida.");
            return;
        }

        //Verifica si el tamaño del archivo es menor al tamaño máximo especificado
        const maxSizeInBytes = maxSizeInMB * 1024 * 1024; // Tamaño máximo en bytes
        if (fileImage.size > maxSizeInBytes) {
            reject(`El archivo seleccionado supera el tamaño máximo de ${maxSizeInMB}MB.`);
            return;
        }

        //Si todas las validaciones pasan.
        resolve(true);
    });
}



export { solicitudPost, solicitudGet, setImageSelected };