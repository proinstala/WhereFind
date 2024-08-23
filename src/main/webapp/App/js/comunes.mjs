
import { mostrarMensaje, mostrarLoading, ocultarLoading, mostrarMensajeAdvertencia } from './alertasSweetAlert2.mjs';

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
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
const solicitudPost = (url, idElement, mostrarLoad) => {
    let data = getDatosForm(idElement);
    return solicitudPostFetch(url, data, idElement, mostrarLoad);
};

/**
 * Realiza una solicitud PUT.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
const solicitudPut = (url, idElement, mostrarLoad) => {
    let data = getDatosForm(idElement);
    return solicitudPutFetch(url, data, idElement, mostrarLoad);
};


/**
 * Realiza una solicitud GET.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
const solicitudGet = (url, idElement, mostrarLoad) => {
    return solicitudGetFetch(url, idElement, mostrarLoad);
};


/**
 * Realiza una solicitud POST utilizando fetch.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} data        - Los datos a enviar en la solicitud.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
function solicitudPostFetch(url, data, idElement, mostrarLoad) {
    formDisable(idElement, true, mostrarLoad);
    return fetch(url, {
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

        return response.json(); // Procesar la respuesta JSON
    })
    .catch(error => {
        throw new Error(error);
    })
    .finally(() => {
        formDisable(idElement, false, false);
    });
}

/**
 * Realiza una solicitud PUT utilizando fetch.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} data        - Los datos a enviar en la solicitud.
 * @param {string} idElement   - Id del formulario.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
function solicitudPutFetch(url, data, idElement, mostrarLoad) {

    formDisable(idElement, true, mostrarLoad);

    return fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.json(); // Procesar la respuesta JSON
    })
    .catch(error => {
        throw new Error(error);
    })
    .finally(() => {
        formDisable(idElement, false, false);
    });
}

/**
 * Realiza una solicitud GET utilizando fetch.
 *
 * @param {string} url         - La URL a la que se enviará la solicitud.
 * @param {string} idElement   - Id del elemento que donde se inyectará los datos.
 * @param {string} mostrarLoad - Bloquea la UI mostrando una alerta con una animación de espera.
 */
function solicitudGetFetch(url, idElement, mostrarLoad) {

    if (mostrarLoad) {
        mostrarLoading();
    }

    return fetch(url, {
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

        return response;
    })
    .catch(error => {
        console.error("Error:", error);
        mostrarMensaje("Error", "No se ha podido realizar la acción por un error en el servidor.", "error");
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

    $("button:not([force-disabed]), input:not([force-disabed]), select:not([force-disabed]), option:not([force-disabed]), textarea:not([force-disabed])", idForm).prop('disabled', disabled);
}

/**
 * Establece una imagen en un contenedor dado a partir de un archivo de imagen seleccionado,
 * actualiza un campo oculto con la representación Base64 de la imagen, y maneja errores
 * mostrando una advertencia y configurando una imagen por defecto si es necesario.
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
        validateImage(fileImage, maxSizeInMB).then(() => {
            const reader = new FileReader(); //Crea un objeto FileReader para leer el contenido del archivo.
            reader.readAsDataURL(fileImage); //Lee el contenido del archivo como una URL de datos.
            reader.onload = function(e) {
                const base64String = e.target.result;   //Obtén la URL de datos Base64
                contenedorImagen.src = base64String;    //Asigna el contenido del archivo como una URL de datos a la imagen.
                inputHideImagen64.value = base64String; //Establece el valor del input oculto con la representación Base64 de la imagen.
                //labelImagen.textContent = fileImage.name;
                resolve(fileImage.name); // Resuelve la promesa en true cuando la imagen se establece correctamente.
            };

        }).catch(error => {
            mostrarMensajeAdvertencia("Imagen no válida", error, "warning");
            if(defaultImage) {
                contenedorImagen.src = defaultImage; //Si no se seleccionó ningún archivo válido, muestra la imagen por defecto.
            }
            reject(error); // Rechaza la promesa en false cuando hay un error.
        });
    });
};

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

/**
 * Restablece todos los campos de un formulario.
 *
 * @param {string} idForm - El selector CSS del formulario a restablecer.
 *
 */
function resetCamposForm(idForm) {
    document.querySelector(idForm).reset();
}

/**
 * Detecta los cambios en los datos de un formulario y si esos datos son distintos al los datos originales del mismo.
 *
 * @param {string} idForm   - Id del formulario que se quiere vigilar para detectar cambios.
 * @param {function} callBack  - Método que recibirá el resultado.
 */
const detectarCambiosFormulario = (idForm, callBack) => {
    let form_original_data = $(idForm).serialize();
    $(idForm).on('keyup change paste', 'input, select, textarea', function(){
        if (callBack !== null) {
            callBack($(idForm).serialize() !== form_original_data);
        }
    });
};



export { solicitudPost, solicitudGet, solicitudPut, setImageSelected, resetCamposForm, detectarCambiosFormulario };