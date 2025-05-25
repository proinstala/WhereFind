
import {solicitudPost, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idInputCalle = "#calle";
const idInputNumero = "#numero";
const idInputCodigoPostal = "#codigoPostal";
const idFormDireccion = "#frmCrearDireccion";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosDireccion = "#btnDeshacerCambiosDireccion";


$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectLocalidad = document.querySelector(idSelectLocalidad);
    const btnDeshacerCambiosDireccion = document.querySelector(idBtnDeshacerCambiosDireccion);
    const btnCancelar = document.querySelector(idBtnCancelar);

    //Añade un evento de cambio al select de provincias y actualiza el select de localidades según la provincia seleccionada.
    const cargaInputSelectLocalidad = () => {
        selectProvincia.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            //Crea un objeto con la información de la provincia seleccionada (id y nombre).
            const jsonProvincia = {
                id: optionSelected.value,
                nombre: optionSelected.textContent
            };

            const jsonProvinciaString = JSON.stringify(jsonProvincia); //Convierte el objeto jsonProvincia a una cadena JSON.
            const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString); //Codifica la cadena JSON para que sea segura al incluirla en la URL.

            //Llama a cargarInputSelect para llenar el select de localidades con los datos obtenidos del select provincia.
            cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, '');
        });
        //onDetectarCambiosCrearDireccion(false);
        detectarCambiosFormulario(idFormDireccion, onDetectarCambiosCrearDireccion);
    };

    //Carga el select provicia.
    cargarInputSelect(selectProvincia, "api/provincia/provincias", 'Seleccione una provincia', false, cargaInputSelectLocalidad);

    validarFormulario(idFormDireccion);


    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/direcciones";
    });

    btnDeshacerCambiosDireccion.addEventListener('click', () => {
        resetCamposForm(idFormDireccion);
        vaciarSelect(selectLocalidad);
        onDetectarCambiosCrearDireccion(false);
        detectarCambiosFormulario(idFormDireccion, onDetectarCambiosCrearDireccion);
    });

});


function onDetectarCambiosCrearDireccion(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosDireccion").prop('disabled', !hayCambios);
}


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            calle: {
                required: true,
                maxlength: 100
            },
            numero: {
                required: true,
                maxlength: 6
            },
            codigoPostal: {
                min: 0,
                max: 999999
            },
            provincia: {
                required: true,
                min: 1,
                max: 999999
            },
            localidad: {
                required: true,
                min: 1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            calle: {
                required: "Debe introducir el nombre de calle.",
                maxlength: "Longitud máx 100 caracteres."
            },
            numero: {
                required: "Debe introducir el numero de la dirección.",
                maxlength: "Longitud máx 6 caracteres."
            },
            codigoPostal: {
                min: "No puede introducir numeros negativos.",
                max: "Valor introducido no válido."
            },
            provincia: {
                required: "Debe seleccionar una provincia.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            localidad: {
                required: "Debe seleccionar una localidad.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const direccionJSON = getDireccionJson();
            solicitudPost(`api/direccion/create`, idForm, true, direccionJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "direccion/direcciones";
                            mostrarMensaje("Dirección Creada.", `Se ha creado la dirección con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getDireccionJson() {
    // Guardar referencias de los elementos del DOM
    const selectLocalidad = document.querySelector(idSelectLocalidad);
    const selectProvincia = document.querySelector(idSelectProvincia);
    const inputCalle = document.querySelector(idInputCalle);
    const inputNumero = document.querySelector(idInputNumero);
    const inputCodigoPostal = document.querySelector(idInputCodigoPostal);

    // Construcción del objeto JSON con validación del código postal
    const direccionJSON = {
        id: "0",
        calle: inputCalle.value.trim(),
        numero: inputNumero.value.trim(),
        codigoPostal: inputCodigoPostal.value.trim() || "0", // Valida código postal vacío y asigna "0"
        localidad: {
            id: selectLocalidad.value,
            nombre: selectLocalidad.selectedOptions[0]?.textContent || "", // Usar ?. para evitar errores si no hay opción seleccionada
            provincia: {
                id: selectProvincia.value,
                nombre: selectProvincia.selectedOptions[0]?.textContent || ""
            }
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        direccionJSON: JSON.stringify(direccionJSON)
    }).toString();

    return data;
}
