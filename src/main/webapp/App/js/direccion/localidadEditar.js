
import { solicitudGet, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idSelectProvincia = "#provincia";
const idInputIdLocalidad = "#localidad_id";
const idInputNombre = "#nombre";
const idFormLocalidad = "#frmModificarLocalidad";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosLocalidad = "#btnDeshacerCambiosLocalidad";

let oldLocalidad;

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const inputIdLocalidad = document.querySelector(idInputIdLocalidad);
    const btnDeshacerCambiosLocalidad = document.querySelector(idBtnDeshacerCambiosLocalidad);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormLocalidad);

    getLocalidad(inputIdLocalidad.value);

    btnDeshacerCambiosLocalidad.addEventListener('click', () => {
        fillFielsLocalidad(oldLocalidad);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/localidades";
    });

});

function onDetectarCambiosModificarLocalidad(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosLocalidad).prop('disabled', !hayCambios);
}


function getLocalidad(idLocalidad) {
    solicitudGet(`api/localidad/localidad?idLocalidad=${idLocalidad}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldLocalidad = response.data;
                    console.log(oldLocalidad);
                    fillFielsLocalidad(oldLocalidad);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsLocalidad(localidad) {
    const form = document.querySelector(idFormLocalidad);

    const inputNombre = form.querySelector(idInputNombre);
    const selectProvincia = form.querySelector(idSelectProvincia);


    inputNombre.value = localidad.nombre;

    cargarInputSelect(selectProvincia, "api/provincia/provincias", '', localidad.provincia.id, () => {
        onDetectarCambiosModificarLocalidad(false);
        detectarCambiosFormulario(idFormLocalidad, onDetectarCambiosModificarLocalidad);
    });
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            },
            provincia: {
                required: true,
                min: 1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre de localidad.",
                maxlength: "Longitud máx 100 caracteres."
            },
            provincia: {
                required: "Debe seleccionar una provincia.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Localidad", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const localidadIdInput = document.querySelector('#localidad_id');
                            const localidadId = localidadIdInput ? localidadIdInput.value : -1;

                            solicitudPut(`api/localidad/update/${localidadId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificada Localidad.", `Se han modificado correctamente los datos de localidad ${response.data.id}`, "success");
                                            oldLocalidad = response.data;
                                            console.log("respuesta: ");
                                            console.log(oldLocalidad);
                                            onDetectarCambiosModificarLocalidad(false);
                                            detectarCambiosFormulario(idFormLocalidad, onDetectarCambiosModificarLocalidad);
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