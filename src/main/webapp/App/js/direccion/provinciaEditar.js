

import { solicitudGet, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idInputIdProvincia = "#provincia_id";
const idInputNombre = "#nombre";
const idFormProvincia = "#frmModificarProvincia";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosProvincia = "#btnDeshacerCambiosProvincia";

let oldProvincia;

$(document).ready(function () {
    const inputIdProvincia = document.querySelector(idInputIdProvincia);
    const btnDeshacerCambiosProvincia = document.querySelector(idBtnDeshacerCambiosProvincia);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormProvincia);

    getProvincia(inputIdProvincia.value);

    btnDeshacerCambiosProvincia.addEventListener('click', () => {
        fillFielsProvincia(oldProvincia);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/provincias";
    });

});

function onDetectarCambiosModificarProvincia(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosProvincia).prop('disabled', !hayCambios);
}


function getProvincia(idProvincia) {
    solicitudGet(`api/provincia/provincia?idProvincia=${idProvincia}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldProvincia = response.data;
                    fillFielsProvincia(oldProvincia);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsProvincia(provincia) {
    const form = document.querySelector(idFormProvincia);

    const inputNombre = form.querySelector(idInputNombre);

    inputNombre.value = provincia.nombre;
    
    onDetectarCambiosModificarProvincia(false);
    detectarCambiosFormulario(idFormProvincia, onDetectarCambiosModificarProvincia); 
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre de la provincia.",
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Provincia", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const provinciaIdInput = document.querySelector(idInputIdProvincia);
                            const provinciaId = provinciaIdInput ? provinciaIdInput.value : -1;

                            solicitudPut(`api/provincia/update/${provinciaId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificada Provincia.", `Se han modificado correctamente los datos de provincia ${response.data.id}`, "success");
                                            oldProvincia = response.data;
                                            onDetectarCambiosModificarProvincia(false);
                                            detectarCambiosFormulario(idFormProvincia, onDetectarCambiosModificarProvincia);
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

