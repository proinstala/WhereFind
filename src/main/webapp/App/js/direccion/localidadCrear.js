
import {solicitudPost, cargarInputSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idSelectProvincia = "#provincia";
const idInputNombre = "#nombre";
const idFormLocalidad = "#frmCrearLocalidad";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosLocalidad = "#btnDeshacerCambiosLocalidad";
const ADMIN = 'Admin';

const User = {
    rol: ""
};


$(document).ready(function () {
    const btnDeshacerCambiosLocalidad = document.querySelector(idBtnDeshacerCambiosLocalidad);
    const btnCancelar = document.querySelector(idBtnCancelar);
    const selectProvincia = document.querySelector(idSelectProvincia);
    
    cargarInputSelect(selectProvincia, "api/provincia/provincias", 'Todas', false, "");

    validarFormulario(idFormLocalidad);

    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/localidades";
    });

    btnDeshacerCambiosLocalidad.addEventListener('click', () => {
        resetCamposForm(idFormLocalidad);
        onDetectarCambiosCrearLocalidad(false);
        detectarCambiosFormulario(idFormLocalidad, onDetectarCambiosCrearLocalidad);
    });
    
    detectarCambiosFormulario(idFormLocalidad, onDetectarCambiosCrearLocalidad);
});


function onDetectarCambiosCrearLocalidad(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosLocalidad").prop('disabled', !hayCambios);
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
            const localidadJSON = getLocalidadJson();
            console.log(localidadJSON);
            solicitudPost(`api/localidad/create`, idForm, true, localidadJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "direccion/localidades";
                            mostrarMensaje("Localidad Creada.", `Se ha creado la localidad con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getLocalidadJson() {
    // Guardar referencias de los elementos del DOM
    const inputNombre = document.querySelector(idInputNombre);
    const selectProvincia = document.querySelector(idSelectProvincia);

    // Construcción del objeto JSON
    const localidadJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        provincia: {
                id: selectProvincia.value,
                nombre: selectProvincia.selectedOptions[0]?.textContent || "" // Usar ?. para evitar errores si no hay opción seleccionada.
            }
        
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        localidadJSON: JSON.stringify(localidadJSON)
    }).toString();

    return data;
}






