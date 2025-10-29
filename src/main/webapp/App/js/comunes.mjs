
import { mostrarMensaje, mostrarLoading, ocultarLoading, mostrarMensajeAdvertencia } from './alertasSweetAlert2.mjs?v=20241021_184300';
import {DISPLAY_TYPES} from './constantes.mjs';

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
 * Realiza una solicitud HTTP POST a una URL especificada.
 *
 * <p>Este método envía una solicitud POST a la URL proporcionada. Puede utilizar datos
 * opcionales que se pasan al método o extraer datos del formulario asociado con el
 * identificador {@code idElement}. Además, puede mostrar una alerta con una animación de
 * espera si se indica {@code mostrarLoad}.</p>
 *
 * @param {string} url         - La URL a la que se enviará la solicitud POST.
 * @param {string} idElement   - El identificador del formulario del cual se extraerán los datos
 *                               si no se proporciona el parámetro {@code data}.
 * @param {boolean} mostrarLoad - Un valor booleano que indica si se debe mostrar una animación
 *                                 de espera mientras se realiza la solicitud.
 * @param {Object} [data]      - Datos opcionales a enviar con la solicitud POST. Si no se
 *                               proporciona, se extraerán los datos del formulario con el
 *                               identificador {@code idElement}.
 * @returns {Promise}          - Una promesa que representa el resultado de la solicitud POST.
 *                               La promesa se resuelve con el resultado de la función
 *                               {@code solicitudPostFetch}.
 */
const solicitudPost = (url, idElement, mostrarLoad, data) => {
    const dataForm = (data)? data : getDatosForm(idElement);
    return solicitudPostFetch(url, dataForm, idElement, mostrarLoad);
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
            //'Content-Type': 'application/json'
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
        //console.log("complete");
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

    //$("button:not([force-disabed]), input:not([force-disabed]), select:not([force-disabed]), option:not([force-disabed]), textarea:not([force-disabed])", idForm).prop('disabled', disabled);

    $("button:not([force-disabed]), input:not([force-disabed]), select:not([force-disabed]), option:not([force-disabed]), textarea:not([force-disabed])",
       $(idForm)).prop('disabled', disabled);
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
 * Restablece los campos de imagen a su valor por defecto.
 * 
 * Esta función reinicia los valores del contenedor de imagen,
 * incluyendo la imagen visible, el valor del input oculto (base64) y el texto
 * con el nombre de la imagen. Si se proporciona `srcImg`, se establece como
 * la nueva fuente de la imagen. Si no, se deja en blanco.
 *
 * @param {HTMLElement} contenedor - El contenedor que incluye el input oculto,
 *                                   la etiqueta <img> y el nombre de la imagen.
 * @param {string} srcImg - La URL o ruta de la imagen por defecto a mostrar.
 */
function resetImg(contenedor, srcImg) {
    if(contenedor) {
        const inputHide = contenedor.querySelector('input[type="hidden"]');
        const imagen = contenedor.querySelector('img');
        const nombre = contenedor.querySelector('span');
        
        nombre.textContent = "";
        inputHide.value = "";
        if(srcImg) {
            imagen.src = srcImg;
        } else {
            imagen.src = "";
        }
    }
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
    //console.log("antes: " + form_original_data);
    $(idForm).on('keyup change paste', 'input, select, textarea', function(){
        if (callBack !== null) {
            //console.log("funcion - old:" + form_original_data);
            //console.log("funcion - new:" + $(idForm).serialize());
            callBack($(idForm).serialize() !== form_original_data);
        }
    });
};

/**
 * Realiza una solicitud GET para obtener datos y cargar un select HTML con las opciones recibidas.
 * 
 * Devuelve una promesa que se resuelve cuando el select ha sido cargado correctamente. Si ocurre un error,
 * la promesa se rechaza con el mensaje de error correspondiente.
 *
 * @param {HTMLElement} nodoInputSelect - El elemento select donde se cargarán las opciones. Debe ser un elemento HTML válido.
 * @param {string} url - La URL a la que se realizará la solicitud GET para obtener los datos.
 * @param {string} [firstOption=''] - (Opcional) Texto para una primera opción a mostrar en el select, usualmente un texto de guía.
 * @param {string} [selectOption] - (Opcional) Valor de la opción que debe seleccionarse automáticamente después de cargar las opciones.
 * @param {Function} [callback] - (Opcional) Función callback que se ejecuta después de llenar el select. Debe ser una función válida.
 */
function cargarInputSelect(nodoInputSelect, url, firstOption = '', selectOption, callback) {
    return new Promise((resolve, reject) => {
        //Validar que el elemento select es válido
        if (!(nodoInputSelect instanceof HTMLElement)) {
            console.error("nodoInputSelect no es un elemento HTML válido.");
            return;
        }

        //Validar que la URL es una cadena no vacía
        if (typeof url !== 'string' || url.trim() === '') {
            console.error("La URL proporcionada no es válida.");
            mostrarMensajeError("Error", "URL no válida.");
            return;
        }

        //Realiza una solicitud GET a la URL especificada.
        solicitudGet(url, "", false)
            .then(response => {
            if (response.isError === 1) {
                mostrarMensajeError("Se ha producido un error", response.result);
                return; // Salir de la función si hay un error
            }
            
            //Llena el elemento select con los datos recibidos y la primera opción opcional.
            fillInputSelect(nodoInputSelect, response.data, firstOption);

            if (selectOption) {
                seleccionarValorSelect(nodoInputSelect, selectOption);
            }

            //Ejecutar el callback si se proporciona y es una función válida
            if (callback && typeof callback === 'function') {
                try {
                    callback();
                } catch (callbackError) {
                    console.error("Error al ejecutar el callback:", callbackError);
                    mostrarMensajeError("Error", "Error al ejecutar la acción posterior.");
                }
            }
            resolve(); // <- Aquí resolvemos la promesa
        })
        .catch(error => {
            //Manejar errores de la solicitud
            console.error("Error:", error);
            mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            reject(error);
        });
    });
}

/**
 * Rellena un elemento select HTML con opciones basadas en los datos proporcionados.
 * 
 * El contenido de las opciones varía dependiendo del ID del elemento select:
 * - Si el ID es 'direccion', se muestra una descripción detallada de la dirección.
 * - Para otros IDs, se muestra únicamente el nombre del elemento.
 *
 * @param {HTMLElement} nodeInputSelect - El elemento select que se va a llenar con las opciones.
 * @param {Array} datos - Un array de objetos con las propiedades 'id' y 'nombre' para crear las opciones.
 * @param {string} optionGenerico - (Opcional) Texto para una opción genérica que se añade al principio del select.
 */
function fillInputSelect(nodeInputSelect, datos, optionGenerico) {
    //Si se proporciona la opción genérica, se agrega como la primera opción del select.
    if(optionGenerico) {
        nodeInputSelect.innerHTML = `<option value="${-1}">${optionGenerico}</option>`;
    } else {
        vaciarSelect(nodeInputSelect);
    }

    const fragment = document.createDocumentFragment(); // Crear un fragmento de documento.
    
    datos.forEach((dato) => {
        const elementOption = document.createElement('OPTION');
        elementOption.setAttribute('value', dato.id);

        switch (nodeInputSelect.id) {
            case 'direccion':
                    // Solo si es 'direccion', mostrar los datos detallados
                    const cp = dato.codigoPostal ?? '';
                    const cpText = cp ? `, C.P. ${cp}` : '';
                    elementOption.textContent = `ID ${dato.id} - ${dato.calle} nº ${dato.numero}${cpText}, ${dato.localidad.nombre}, ${dato.localidad.provincia.nombre}`;
                    break;
            case 'articuloProveedor':
                elementOption.textContent = `${dato.proveedor.nombre} - precio: ${dato.precio}`;
                elementOption.setAttribute("data-precio", dato.precio);
                elementOption.setAttribute('value', dato.proveedor.id);
                break;
            case 'articulo':
                elementOption.textContent = `${dato.nombre} [marca: ${dato.marca.nombre}] [ref: ${dato.referencia}]`;
                break;
            default:
                // Para cualquier otro select, solo mostrar el nombre
                elementOption.textContent = dato.nombre;
                break;
        }
        
        fragment.appendChild(elementOption);
    });
    

    //Agregar el fragmento completo al select, actualizando su contenido en una sola operación.
    nodeInputSelect.appendChild(fragment);
}


/**
 * Selecciona un valor en un nodo <select> y devuelve una promesa.
 * @param {HTMLSelectElement} selectNode - El nodo <select> en el que se desea seleccionar un valor.
 * @param {string} value - El valor que se desea seleccionar.
 * @returns {Promise<void>} - Una promesa que se resuelve cuando se selecciona el valor.
 */
function seleccionarValorSelect(selectNode, value) {
    return new Promise((resolve, reject) => {
        if (!(selectNode instanceof HTMLSelectElement)) {
            reject(new Error("El primer argumento debe ser un nodo select."));
            return;
        }

        // Busca y selecciona la opción con el valor especificado
        const optionToSelect = Array.from(selectNode.options).find(
            (option) => option.value === value.toString(10)
        );

        if (optionToSelect) {
            selectNode.value = value;
            
            //Disparar evento 'change' para que los listeners reaccionen
            const event = new Event('change', { bubbles: true });
            selectNode.dispatchEvent(event);
            
            resolve(); // Resuelve la promesa si el valor fue seleccionado correctamente
        } else {
            reject(new Error(`El valor "${value}" no se encontró en el select ${selectNode.name}`));
        }
    });
}

/**
 * Vacía todas las opciones de un elemento <select> en el DOM.
 *
 * @param {HTMLSelectElement} nodeInputSelect - El nodo <select> que se desea vaciar.
 *                                              Debe ser un elemento HTMLSelectElement válido.
 */
function vaciarSelect(nodeInputSelect) {
    nodeInputSelect.options.length = 0; // Establece la longitud de las opciones del <select> a 0, eliminando todas las opciones
}

/**
 * Función que asigna un evento de clic a cada fila de una tabla para que se pueda seleccionar.
 * @param {Element} nodeTableBody - El elemento <tbody> de la tabla cuyo evento de clic se asignará a cada fila.
 */
function addRowSelected(nodeTableBody) {
    const rows = nodeTableBody.querySelectorAll('tr');
    rows.forEach(row => {
       row.addEventListener('click', markRow); //Al hacer clic, se llamará a la función markRow
    });
}

/**
 * Función que maneja el evento de selección de una fila de la tabla.
 * @param {Event} event - El objeto del evento que contiene información sobre el clic.
 */
function markRow(event) {
    //Obtener la fila seleccionada a través del evento
    const row = event.currentTarget;

    const tableBody = row.parentElement;
    const table = tableBody.parentElement;

    //Obtener todas las filas del mismo tbody (padre)
    const rows = tableBody.querySelectorAll('tr');

    resetRowsSelected(rows); //Reiniciar. Remover la clase ('selected') de todas las filas
    table.setAttribute('data-rowselected', row.id);  //Asignar el ID de la fila seleccionada al atributo ('data-rowSelected') de la tabla padre de la fila.
    row.classList.add("selected"); //Añadir la clase 'selected' a la fila actual para marcarla como seleccionada
}

/**
 * Función que reinicia la selección de todas las filas eliminando la clase 'selected'.
 * @param {NodeList} rows - Una lista de nodos de todas las filas de la tabla.
 */
function resetRowsSelected(rows) {
    rows.forEach(row => row.classList.remove("selected"));
}

/**
 * Observa los cambios en el atributo 'data-rowSelected' de una tabla y ejecuta un callback
 * cuando el valor del atributo cambia.
 * @param {HTMLElement} tabla - El elemento HTML (tabla) que se va a observar.
 * @param {Function} callback - La función callback que se ejecutará cuando el atributo cambie.
 */
function observeRowSelectedChange(tabla, callback) {
    //Verifica que la tabla sea un elemento HTML válido
    if (!tabla || !(tabla instanceof HTMLElement)) {
        console.error('El elemento proporcionado no es un nodo HTML válido.');
        return; //Sale de la función si la tabla no es válida
    }

    /**
     * Función que se ejecuta cuando se detectan cambios en los atributos del elemento observado.
     * @param {MutationRecord[]} mutationsList - Lista de objetos MutationRecord que describen los cambios detectados.
     */
    function handleAttributeChange(mutationsList) {
        for (let mutation of mutationsList) {
            //Verifica si el tipo de mutación es de atributos y si el atributo modificado es 'data-rowselected'
            if (mutation.type === 'attributes' && mutation.attributeName === 'data-rowselected') {
                // Obtener el nuevo valor del atributo
                const nuevoValor = tabla.getAttribute('data-rowselected'); //data-rowSelected
                //Ejecuta el callback con un valor booleano indicando si el nuevo valor es mayor a 0
                callback(nuevoValor > 0);
            }
        }
    }

    //Crea un observador de mutaciones configurado para ejecutar la función handleAttributeChange
    const observer = new MutationObserver(handleAttributeChange);

    //Configuración del observador: observar cambios en los atributos del elemento
    const config = { attributes: true };

    //Inicia el observador sobre la tabla con las opciones configuradas
    observer.observe(tabla, config);
}

/**
 * Elimina la fila seleccionada en una tabla específica.
 *
 * Esta función busca una tabla por su ID, verifica qué fila está seleccionada
 * según el atributo `data-rowselected` en la tabla, y luego elimina dicha fila
 * si se encuentra en el `tbody` de la tabla.
 *
 * @param {string} idTabla - El selector de la tabla que contiene la fila a eliminar.
 *                            Debe ser un selector CSS, por ejemplo: '#miTabla'.
 *
 */
function deleteRowSelectedTable(idTabla) {
    // Selecciona la tabla por su ID
    const tabla = document.querySelector(idTabla);

    if (tabla) {
        const idDireccion = tabla.getAttribute('data-rowselected'); //data-rowSelected

        // Selecciona el tbody de la tabla
        const tbody = tabla.querySelector('tbody');

        // Verifica si el tbody existe
        if (tbody) {
            // Obtiene todas las filas (tr) dentro del tbody
            const filas = tbody.querySelectorAll('tr');

            // Recorre cada fila usando forEach
            filas.forEach(fila => {
                // Verifica si la fila tiene el ID que estamos buscando
                if (fila.id === idDireccion) {
                    // Si encontramos la fila, la eliminamos
                    fila.remove();
                    tabla.setAttribute('data-rowselected', -1);
                    console.log(`Fila con id ${idDireccion} eliminada.`);
                }
            });
        } else {
            console.log('No se encontró el tbody en la tabla.');
        }
    } else {
        console.log(`No se encontró la tabla con id ${idTabla}.`);
    }
}

/**
 * Muestra un contenedor específico y oculta el contenedor padre del botón clicado.
 *
 * @param {Event} event - El evento del botón que fue clicado.
 * @param {string} nameContenedor - El valor del atributo `name` del contenedor padre a ocultar (ej: "contenedorDatos").
 * @param {string} idContenedorMostrar - El selector del ID del contenedor que se desea mostrar (ej: "#contenedorProveedor").
 * @param {string} display - El valor CSS que se usará para mostrar el contenedor (ej: "grid", "block", etc.). por defecto, grid.
 */
function mostrarContenedor(event, nameContenedor, idContenedorMostrar, display = DISPLAY_TYPES.GRID) {
    // Obtener el botón que disparó el evento
    const boton = event.currentTarget;

    // Buscar el contenedor padre más cercano con el atributo name especificado
    const contenedorOcultar = boton.closest(`[name="${nameContenedor}"]`);

    // Ocultar el contenedor padre si se encontró
    if (contenedorOcultar) {
        contenedorOcultar.style.display = DISPLAY_TYPES.NONE;
    } else {
        console.warn("No se encontró el contenedor con name='contenedorDatos'");
        return;
    }

    // Buscar el contenedor a mostrar por su ID
    const contenedorMostrar = document.querySelector(idContenedorMostrar);

    // Mostrar el contenedor objetivo si se encontró
    if (contenedorMostrar) {
        contenedorMostrar.style.display = display;
    } else {
        contenedorOcultar.style.display = display;
        console.warn(`No se encontró el contenedor con ID: ${idContenedorMostrar}`);
    }
}


export { solicitudPost,
        solicitudGet,
        solicitudPut,
        setImageSelected,
        resetImg,
        resetCamposForm,
        detectarCambiosFormulario,
        getDatosForm,
        addRowSelected,
        fillInputSelect,
        cargarInputSelect,
        observeRowSelectedChange,
        seleccionarValorSelect,
        vaciarSelect,
        deleteRowSelectedTable,
        mostrarContenedor
        };