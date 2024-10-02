import { setImageSelected, solicitudGet, solicitudPut, resetCamposForm, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';

let datosOriginalesFormulario;


function setImgSrc(json, idKeyJson, idImg) {
    if (json.hasOwnProperty(idKeyJson)) {
        const imgElement = document.querySelector(`[id="${idImg}"]`);
        if (imgElement) {
            imgElement.src = json[idKeyJson];
        }
    }
}

function setFormValue(json, idKeyJson, idInput) {
    if (json.hasOwnProperty(idKeyJson)) {
        const inputElement = document.querySelector(`[id="${idInput}"]`);
        if (inputElement) {
            inputElement.value = json[idKeyJson];
        }
    }
}


$(document).ready(function () {
    const idFormModificarUsuario = '#frmModificarUsuario';
    const idFormModificarPassword = '#frmModificarPassword';
    const usuarioIdInput = document.querySelector('#usuario_id');
    const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

    const usuarioNeddLoadInput = document.querySelector('#usuario_need_load');
    const usuarioNeedLoad = usuarioNeddLoadInput ? usuarioNeddLoadInput.value : 0;


    const continuarCarga = () => {

        datosOriginalesFormulario = getDatosOriginalesForm(idFormModificarUsuario);

        validarFormulario(idFormModificarUsuario);
        validarFormularioPassword(idFormModificarPassword);

        detectarCambiosFormulario(idFormModificarUsuario, onDetectarCambiosModificarUsuario);
        detectarCambiosFormulario(idFormModificarPassword, onDetectarCambiosModificarPassword);

        const btnCancelar = document.querySelector('#btnCancelar');
        const btnCancelarPassword = document.querySelector('#btnCancelarPassword');
        const btnDeshacerCambiosUsuario = document.querySelector('#btnDeshacerCambiosUsuario');
        const btnDeshacerCambiosPassword = document.querySelector('#btnDeshacerCambiosPassword');
        const btnPassword = document.querySelector('#btnPassword');
        const btnUsuario = document.querySelector('#btnUsuario');

        const btnFoto = document.querySelector('#btnFoto');
        const contenedorImg = document.querySelector('#imgUsuario');
        const inputHide64 = document.querySelector('#imagenUsuarioB64');
        const labelInputFoto = document.querySelector('#textoImagen');

        const divFormUsuario = document.querySelector('#form_usuario');
        const divFormPassword = document.querySelector('#form_password');

        btnFoto.addEventListener('change', (e) => {
            const defaultUserImg = contenedorImg.src;
            const fileImg = e.target.files[0];

            //Establece la imagen seleccionada.
            setImageSelected(fileImg, contenedorImg, inputHide64, defaultUserImg, 2)
            .then((result) => {
                if (result) {
                    console.log("Imagen establecida correctamente.");

                    // Detecta el cambio de la imagen
                    onDetectarCambiosModificarUsuario(labelInputFoto.textContent !== result);

                    labelInputFoto.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                btnFoto.value = '';
            });
        });

        btnCancelar.addEventListener('click', () => {
            window.location.href = btnCancelar.dataset.uri;
        });

        btnCancelarPassword.addEventListener('click', () => {
            window.location.href = btnCancelarPassword.dataset.uri;
        });

        btnDeshacerCambiosUsuario.addEventListener('click', () => {
            setDatosForm(idFormModificarUsuario, datosOriginalesFormulario, '#imagenUsuarioB64');
    });

    btnDeshacerCambiosPassword.addEventListener('click', () => {
        resetCamposForm(idFormModificarPassword);
    });

    btnPassword.addEventListener('click',() => mostrarContenedor(divFormPassword, divFormUsuario, 'grid'));
    btnUsuario.addEventListener('click',() => mostrarContenedor(divFormUsuario, divFormPassword, 'grid'));
};

    if (usuarioNeedLoad == 1 && usuarioId > 0) {
        solicitudGet(`api/identidad/user/${usuarioId}`, idFormModificarUsuario, true)
        .then(response => {
            if (response.isError === 1) {
                mostrarMensajeError("No se ha podido obtener los datos del usuario", response.result);
            } else {
                setFormValue(response.data, "apellidos", "apellidoRealUsuario");
                setFormValue(response.data, "email", "emailUsuario");
                setFormValue(response.data, "imagen", "imagenUsuarioB64");
                setFormValue(response.data, "nombre", "nombreRealUsuario");
                setFormValue(response.data, "rol", "rolUsuario");
                setFormValue(response.data, "userName", "nombreUsuario");
                setImgSrc(response.data, "imagen", "imgUsuario");

                continuarCarga();
            }
        })
        .catch(error => {
            // Maneja el error aquí
            console.error("Error:", error);
            mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            continuarCarga();
        });

    }
    else {
        continuarCarga();
    }




});

function onDetectarCambiosModificarUsuario(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosUsuario").prop('disabled', !hayCambios);
}

function onDetectarCambiosModificarPassword(hayCambios) {
    $("#btnGuardarPassword").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosPassword").prop('disabled', !hayCambios);
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 20
            },
            nombreRealUsuario: {
                required: true,
                maxlength: 100
            },
            apellidoRealUsuario: {
                required: true,
                maxlength: 100
            },
            emailUsuario: {
                required: true,
                maxlength: 200,
                email: true
            }
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            nombreRealUsuario: {
                required: "Debe introducir su nombre.",
                maxlength: "Longitud máx 100 caracteres."
            },
            apellidoRealUsuario: {
                required: "Debe introducir sus apellidos.",
                maxlength: "Longitud máx 100 caracteres."
            },
            emailUsuario: {
                required: "Debe introducir su email.",
                maxlength: "Longitud máx 200 caracteres.",
                email: "Debe introducir un email válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Usuario", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const usuarioIdInput = document.querySelector('#usuario_id');
                            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

                            solicitudPut(`api/identidad/update/${usuarioId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            //const acceso = () => window.location.replace(response.result);
                                            mostrarMensaje("Modificado Usuario.", `Se han modificado correctamente los datos el usuario de ${response.data.nombre}`, "success");
                                            datosOriginalesFormulario = getDatosOriginalesForm(idForm);
                                        }
                                    })
                                    .catch(error => {
                                        // Maneja el error aquí
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function validarFormularioPassword(idForm) {
    $(idForm).validate({
        rules: {
            passwordUsuario: {
                required: true,
                maxlength: 60
            },
            nuevoPassword: {
                required: true,
                maxlength: 60,
                minlength: 4
            },
            confirmPassword: {
                required: true,
                equalTo: "#nuevoPassword"
            }

        },//Fin de reglas ----------------
        messages: {
            passwordUsuario: {
                required: "Este campo es requerido.",
                maxlength: "Longitud máx 60 caracteres."
            },
            nuevoPassword: {
                required: "Este campo es requerido.",
                maxlength: "Longitud máx 60 caracteres.",
                minlength: "Logitud minima de 4 caracteres."
            },
            confirmPassword: {
                required: "Este campo es requerido.",
                equalTo: "El password de confirmación es erroneo."
            }

        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Password Usuario", '¿Quieres realmente modificar el password de usuario?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const usuarioIdInput = document.querySelector('#passwordUsuario_id');
                            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

                            solicitudPut(`api/identidad/updatePassword/${usuarioId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado Usuario.", `Se han modificado correctamente los datos el usuario de ${response.data.nombre}`, "success");
                                            resetCamposForm(idForm);
                                        }
                                    })
                                    .catch(error => {
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function mostrarContenedor(nodeMostrar, nodeOcultar, display) {
    const estiloNodeMostrar = window.getComputedStyle(nodeMostrar);

    // Verificar si el nodo a mostrar tiene display: none
    if (estiloNodeMostrar.display === 'none') {
        // Si es así, cambiar su display a grid
        nodeMostrar.style.display = display;

        // Ocultar el nodo a ocultar
        nodeOcultar.style.display = 'none';
    }
}

/**
 * Obtiene los datos originales del formulario, excluyendo los campos de tipo 'hidden' y 'file'.
 * También incluye la URL de la imagen si hay un elemento <img> dentro del formulario.
 *
 * @param {string} idFormulario - El identificador (selector) del formulario del cual obtener los datos.
 * @returns {Array<Object>} Un array de objetos que contienen el nombre y valor de cada campo del formulario.
 *                          Si hay una imagen en el formulario, se agrega un objeto con el nombre del la etiqueta img y su URL como valor.
 */
function getDatosOriginalesForm(idFormulario) {
    const form = document.querySelector(idFormulario);
    const elements = form.querySelectorAll('input, textarea');

    //Selecciona el elemento img dentro del div con clase 'form__input'
    const imageElement = form.querySelector('.form__input img');

    //Filtrar los inputs para excluir los de tipo 'hidden' y 'file'
    const filteredElements = Array.from(elements).filter(element => {
        return element.type !== 'hidden' && element.type !== 'file';
    });

    //Crea un array de datos a partir de los elementos filtrados.
    const dataArray = filteredElements.map(element => ({
        name: element.name,
        value: element.type === 'checkbox' || element.type === 'radio' ? element.checked : element.value
    }));

    //Si hay un elemento de imagen, se agrega al array de datos.
    if (imageElement) {
        dataArray.push({
            name: imageElement.name, // Nombre del campo para la imagen
            value: imageElement.src // URL de la imagen
        });
    }

    return dataArray;
}

/**
 * Establece los valores de los campos de un formulario basado en un array de datos.
 *
 * @param {string} idFormulario - El identificador del formulario en el cual se establecerán los datos.
 * @param {Array} dataArray - Un array de objetos que contiene el nombre y el valor de cada campo que se va a establecer en el formulario.
 * @param {string} idInputHideImeagen64 - El identificador del input oculto que almacenará la URL de la imagen en formato base64.
 */
function setDatosForm(idFormulario, dataArray, idInputHideImeagen64) {
    const form = document.querySelector(idFormulario);
    const textoImg = form.querySelector('.form__input #textoImagen');
    const inputHideImg = form.querySelector(idInputHideImeagen64);

    dataArray.forEach(data => {
        const element = form.querySelector(`[name="${data.name}"]`);

        if (element) {
            // Si el input es de tipo checkbox o radio, usa la propiedad checked
            if (element.type === 'checkbox' || element.type === 'radio') {
                element.checked = data.value === true || data.value === "true";
            } else if(element.tagName === 'IMG'){
                element.src = data.value;
                if(textoImg) {
                    textoImg.textContent = "";
                }
                if(inputHideImg) {
                    inputHideImg.value = data.value;
                }
            } else {
                //Para otros tipos de inputs (text, email, etc.), establece el valor normalmente
                element.value = data.value;
            }
        }
    });
}
