
import { solicitudGet, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idInputIdDireccion = "#direccion_id";
const idInputCalle = "#calle";
const idInputNumero = "#numero";
const idInputCodigoPostal = "#codigoPostal";
const idFormDireccion = "#frmModificarDireccion";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosDireccion = "#btnDeshacerCambiosDireccion";

let oldDireccion;

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectLocalidad = document.querySelector(idSelectLocalidad);
    const inputIdDireccion = document.querySelector(idInputIdDireccion);
    const btnDeshacerCambiosDireccion = document.querySelector(idBtnDeshacerCambiosDireccion);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormDireccion);

    getDireccion(inputIdDireccion.value);

    btnDeshacerCambiosDireccion.addEventListener('click', () => {
        fillFielsDireccion(oldDireccion);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion/direcciones";
    });

});

function onDetectarCambiosModificarDireccion(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosDireccion).prop('disabled', !hayCambios);
}


function getDireccion(idDireccion) {
    solicitudGet(`api/direccion/direccion?idDireccion=${idDireccion}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldDireccion = response.data;
                    console.log(oldDireccion);
                    fillFielsDireccion(oldDireccion);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsDireccion(direccion) {
    const form = document.querySelector(idFormDireccion);

    const inputCalle = form.querySelector(idInputCalle);
    const inputNumero = form.querySelector(idInputNumero);
    const inputCodigoPostal = form.querySelector(idInputCodigoPostal);
    const selectProvincia = form.querySelector(idSelectProvincia);
    const selectLocalidad = form.querySelector(idSelectLocalidad);

    inputCalle.value = direccion.calle;
    inputNumero.value = direccion.numero;
    if(direccion.codigoPostal) {
        inputCodigoPostal.value = direccion.codigoPostal;
    }
    /*
    cargarInputSelect(selectProvincia, "api/provincia/provincias", '', direccion.localidad.provincia.id, () => {
        //Crea un objeto con la información de la provincia seleccionada (id y nombre).
        const jsonProvincia = {
            id: selectProvincia.value,
            nombre: selectProvincia.textContent
        };

        const jsonProvinciaString = JSON.stringify(jsonProvincia); //Convierte el objeto jsonProvincia a una cadena JSON.
        const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString); //Codifica la cadena JSON para que sea segura al incluirla en la URL.

        //Llama a cargarInputSelect para llenar el select de localidades con los datos obtenidos del select provincia.
        cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, '', direccion.localidad.id, () => {
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
                cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, 'Seleccione');
            });

            onDetectarCambiosModificarDireccion(false);
            detectarCambiosFormulario(idFormDireccion, onDetectarCambiosModificarDireccion);

        });
    }); 
    */
   
    cargarInputSelect(selectProvincia, "api/provincia/provincias", '', direccion.localidad.provincia.id)
        .then(() => {
            const jsonProvincia = {
                id: selectProvincia.value,
                nombre: selectProvincia.selectedOptions[0].textContent
            };
            const encodedJsonProvincia = encodeURIComponent(JSON.stringify(jsonProvincia));

            return cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, '', direccion.localidad.id);
        })
        .then(() => {
            selectProvincia.addEventListener('change', (e) => {
                const optionSelected = e.target.selectedOptions[0];
                const nuevaProvincia = {
                    id: optionSelected.value,
                    nombre: optionSelected.textContent
                };
                const nuevaProvinciaEncoded = encodeURIComponent(JSON.stringify(nuevaProvincia));

                cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${nuevaProvinciaEncoded}`, 'Seleccione');
            });

            onDetectarCambiosModificarDireccion(false);
            detectarCambiosFormulario(idFormDireccion, onDetectarCambiosModificarDireccion);
        })
        .catch((error) => {
            console.error("Error al cargar dirección:", error);
            mostrarMensajeError("Error", "No se pudo cargar la información de dirección correctamente.");
        });
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
            mostrarMensajeOpcion("Modificar Dirección", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const direccionIdInput = document.querySelector('#direccion_id');
                            const direccionId = direccionIdInput ? direccionIdInput.value : -1;

                            solicitudPut(`api/direccion/update/${direccionId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificada Dirección.", `Se han modificado correctamente los datos de dirección ${response.data.id}`, "success");
                                            oldDireccion = response.data;
                                            console.log("respuesta: ");
                                            console.log(oldDireccion);
                                            onDetectarCambiosModificarDireccion(false);
                                            detectarCambiosFormulario(idFormDireccion, onDetectarCambiosModificarDireccion);
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