
import {solicitudPost, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idInputNombre = "#nombre";
const idFormProvincia = "#frmCrearProvincia";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosProvincia = "#btnDeshacerCambiosProvincia";


$(document).ready(function () {
    const btnDeshacerCambiosProvincia = document.querySelector(idBtnDeshacerCambiosProvincia);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormProvincia);

    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/provincias";
    });

    btnDeshacerCambiosProvincia.addEventListener('click', () => {
        resetCamposForm(idFormProvincia);
        onDetectarCambiosCrearProvincia(false);
        detectarCambiosFormulario(idFormProvincia, onDetectarCambiosCrearProvincia);
    });
    
    detectarCambiosFormulario(idFormProvincia, onDetectarCambiosCrearProvincia);
});


function onDetectarCambiosCrearProvincia(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosProvincia").prop('disabled', !hayCambios);
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
            const provinciaJSON = getProvinciaJson();
            console.log(provinciaJSON);
            solicitudPost(`api/provincia/create`, idForm, true, provinciaJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "direccion/provincias";
                            mostrarMensaje("Provincia Creada.", `Se ha creado la provincia con id "${response.data.id}" correctamente`, "success", redireccionar);
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function getProvinciaJson() {
    // Guardar referencias de los elementos del DOM
    const inputNombre = document.querySelector(idInputNombre);

    // Construcción del objeto JSON
    const provinciaJSON = {
        id: "0",
        nombre: inputNombre.value.trim()
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        provinciaJSON: JSON.stringify(provinciaJSON)
    }).toString();

    return data;
}



